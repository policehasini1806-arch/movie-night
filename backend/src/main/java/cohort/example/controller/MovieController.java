package cohort.example.controller;

import cohort.example.service.TMDBService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/movies")
@RequiredArgsConstructor
public class MovieController {

    private final TMDBService tmdbService;

    @GetMapping("/genre/{genre}")
    public ResponseEntity<List<Map<String, Object>>> getMoviesByGenre(
            @PathVariable String genre,
            @RequestParam(defaultValue = "1") int page) {
        List<Map<String, Object>> movies = tmdbService.getMoviesByGenre(genre, page);
        return ResponseEntity.ok(movies);
    }

    // imdbId format: tt1234567
    @GetMapping("/{imdbId}")
    public ResponseEntity<Map<String, Object>> getMovieDetails(@PathVariable String imdbId) {
        Map<String, Object> movie = tmdbService.getMovieDetails(imdbId);
        return ResponseEntity.ok(movie);
    }
}
