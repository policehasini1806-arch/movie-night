package cohort.example.service;

import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponentsBuilder;

import java.util.*;

@Service
@RequiredArgsConstructor
public class TMDBService {

    @Value("${omdb.api.key}")
    private String apiKey;

    @Value("${omdb.api.base-url}")
    private String baseUrl;

    private final RestTemplate restTemplate = new RestTemplate();

    private static final Map<String, List<String>> GENRE_KEYWORDS = Map.ofEntries(

        Map.entry("Action", List.of(
            "john wick",
            "mad max",
            "baahubali",
            "kgf",
            "vikram 2022",
            "pathaan",
            "war 2019",
            "the raid",
            "oldboy"
        )),

        Map.entry("Comedy", List.of(
            "superbad",
            "home alone",
            "hera pheri",
            "golmaal",
            "oh my god",
            "ala vaikunthapurramuloo",
            "extreme job",
            "welcome 2007",
            "andaz apna apna"
        )),

        Map.entry("Drama", List.of(
            "shawshank redemption",
            "forrest gump",
            "taare zameen par",
            "dangal",
            "mahanati",
            "parasite",
            "3 idiots",
            "drishyam",
            "udaan 2010"
        )),

        Map.entry("Horror", List.of(
            "the conjuring",
            "get out",
            "tumbbad",
            "stree 2018",
            "pizza 2012",
            "train to busan",
            "ju-on the grudge",
            "bhool bhulaiyaa",
            "raat akeli hai"
        )),

        Map.entry("Romance", List.of(
            "titanic",
            "la la land",
            "dilwale dulhania",
            "kabir singh",
            "fidaa",
            "my sassy girl",
            "jab we met",
            "ek villain",
            "96 2018"
        )),

        Map.entry("Thriller", List.of(
            "silence of the lambs",
            "gone girl",
            "andhadhun",
            "drishyam 2015",
            "kahaani",
            "ratsasan",
            "a wednesday",
            "oldboy 2003",
            "memories 2021"
        )),

        Map.entry("Sci-Fi", List.of(
            "interstellar",
            "inception",
            "matrix",
            "enthiran",
            "krrish",
            "ra one",
            "snowpiercer",
            "arrival 2016",
            "2.0 2018"
        )),

        Map.entry("Adventure", List.of(
            "indiana jones",
            "jurassic park",
            "RRR 2022",
            "baahubali 2",
            "the mummy",
            "ponniyin selvan",
            "king kong",
            "adipurush",
            "jungle book"
        )),

        Map.entry("Animation", List.of(
            "toy story",
            "spirited away",
            "your name",
            "finding nemo",
            "the lion king",
            "my neighbor totoro",
            "frozen",
            "howl moving castle",
            "up pixar"
        )),

        Map.entry("Crime", List.of(
            "godfather",
            "goodfellas",
            "gangs of wasseypur",
            "singham",
            "vikram vedha",
            "memories of murder",
            "special 26",
            "article 15",
            "once upon time mumbai"
        )),

        Map.entry("Documentary", List.of(
            "planet earth",
            "free solo",
            "jiro dreams sushi",
            "blackfish",
            "super size me",
            "the social dilemma",
            "wild wild country",
            "icarus 2017",
            "samsara 2011"
        )),

        Map.entry("Fantasy", List.of(
            "lord of the rings",
            "harry potter",
            "baahubali",
            "narnia",
            "the hobbit",
            "princess mononoke",
            "brahmastra",
            "makkhi 2012",
            "adipurush"
        )),

        Map.entry("Mystery", List.of(
            "knives out",
            "prestige",
            "andhadhun",
            "drishyam",
            "gone girl",
            "detective byomkesh bakshy",
            "seven 1995",
            "zodiac 2007",
            "memories 2021"
        ))
    );

    @SuppressWarnings("unchecked")
    public List<Map<String, Object>> getMoviesByGenre(String genreName, int page) {
        List<String> keywords = GENRE_KEYWORDS.getOrDefault(genreName,
                List.of("best movie", "award winning film"));

        List<Map<String, Object>> allMovies = new ArrayList<>();
        Set<String> seenIds = new HashSet<>();

        for (String keyword : keywords) {
            if (allMovies.size() >= 15) break;
            try {
                String searchUrl = UriComponentsBuilder.fromHttpUrl(baseUrl)
                        .queryParam("apikey", apiKey)
                        .queryParam("s", keyword)
                        .queryParam("type", "movie")
                        .build()
                        .toUriString();

                Map<String, Object> searchResponse = restTemplate.getForObject(searchUrl, Map.class);
                if (searchResponse == null || !"True".equals(searchResponse.get("Response"))) continue;

                List<Map<String, Object>> searchResults =
                        (List<Map<String, Object>>) searchResponse.get("Search");
                if (searchResults == null || searchResults.isEmpty()) continue;

                Map<String, Object> top = searchResults.get(0);
                String imdbId = (String) top.get("imdbID");
                if (imdbId == null || seenIds.contains(imdbId)) continue;

                Map<String, Object> detail = fetchMovieDetail(imdbId);
                if (!detail.isEmpty()) {
                    seenIds.add(imdbId);
                    allMovies.add(detail);
                }

            } catch (Exception e) {
                // continue to next keyword
            }
        }

        return allMovies.isEmpty() ? getFallbackMovies() : allMovies;
    }

    public Map<String, Object> getMovieDetails(String imdbId) {
        return fetchMovieDetail(imdbId);
    }

    @SuppressWarnings("unchecked")
    private Map<String, Object> fetchMovieDetail(String imdbId) {
        try {
            String url = UriComponentsBuilder.fromHttpUrl(baseUrl)
                    .queryParam("apikey", apiKey)
                    .queryParam("i", imdbId)
                    .queryParam("plot", "short")
                    .build()
                    .toUriString();

            Map<String, Object> raw = restTemplate.getForObject(url, Map.class);
            if (raw == null || !"True".equals(raw.get("Response"))) return Collections.emptyMap();

            Map<String, Object> movie = new HashMap<>();
            movie.put("id",          raw.get("imdbID"));
            movie.put("title",       raw.get("Title"));
            movie.put("overview",    raw.get("Plot"));
            movie.put("releaseDate", raw.get("Year"));
            movie.put("director",    raw.get("Director"));
            movie.put("actors",      raw.get("Actors"));
            movie.put("genre",       raw.get("Genre"));
            movie.put("runtime",     raw.get("Runtime"));
            movie.put("language",    raw.get("Language"));
            movie.put("country",     raw.get("Country"));

            String lang = (String) raw.get("Language");
            movie.put("langFlag", getLanguageFlag(lang));

            String imdbRating = (String) raw.get("imdbRating");
            try {
                movie.put("rating", imdbRating != null && !imdbRating.equals("N/A")
                        ? Double.parseDouble(imdbRating) : null);
            } catch (NumberFormatException e) {
                movie.put("rating", null);
            }

            String poster = (String) raw.get("Poster");
            movie.put("posterUrl", (poster != null && !poster.equals("N/A")) ? poster : null);

            return movie;

        } catch (Exception e) {
            return Collections.emptyMap();
        }
    }

    private String getLanguageFlag(String language) {
        if (language == null) return "🌍";
        String l = language.toLowerCase();
        if (l.contains("hindi"))    return "🇮🇳";
        if (l.contains("telugu"))   return "🇮🇳";
        if (l.contains("tamil"))    return "🇮🇳";
        if (l.contains("malayalam")) return "🇮🇳";
        if (l.contains("kannada"))  return "🇮🇳";
        if (l.contains("korean"))   return "🇰🇷";
        if (l.contains("japanese")) return "🇯🇵";
        if (l.contains("french"))   return "🇫🇷";
        if (l.contains("spanish"))  return "🇪🇸";
        if (l.contains("english"))  return "🇺🇸";
        return "🌍";
    }

    private List<Map<String, Object>> getFallbackMovies() {
        String[][] data = {
            {"The Shawshank Redemption", "1994", "9.3", "Two imprisoned men find redemption.",                          "English", "🇺🇸"},
            {"Baahubali: The Beginning", "2015", "8.0", "An epic tale of two brothers in ancient India.",               "Telugu",  "🇮🇳"},
            {"3 Idiots",                 "2009", "8.4", "Two friends search for their long-lost companion.",             "Hindi",   "🇮🇳"},
            {"Parasite",                 "2019", "8.5", "A poor family schemes to become employed by a wealthy family.", "Korean",  "🇰🇷"},
            {"Spirited Away",            "2001", "8.6", "A girl enters a world ruled by gods and witches.",              "Japanese","🇯🇵"},
            {"Dangal",                   "2016", "8.3", "A wrestler trains his daughters to be world champions.",        "Hindi",   "🇮🇳"}
        };
        List<Map<String, Object>> list = new ArrayList<>();
        for (String[] d : data) {
            Map<String, Object> m = new HashMap<>();
            m.put("id",          "tt000000" + (int)(Math.random() * 9));
            m.put("title",       d[0]);
            m.put("releaseDate", d[1]);
            m.put("rating",      Double.parseDouble(d[2]));
            m.put("overview",    d[3]);
            m.put("language",    d[4]);
            m.put("langFlag",    d[5]);
            m.put("posterUrl",   null);
            list.add(m);
        }
        return list;
    }
}