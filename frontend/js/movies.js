const API = 'https://movie-night-efnx.onrender.com/api';
let currentPage = 1;
let currentGenre = '';

document.addEventListener('DOMContentLoaded', () => {
    currentGenre = sessionStorage.getItem('winnerGenre') || 'Action';
    document.getElementById('nav-genre').textContent  = currentGenre;
    document.getElementById('page-genre').textContent = currentGenre;
    loadMovies(currentGenre, currentPage);
});

async function loadMovies(genre, page) {
    if (page === 1) {
        document.getElementById('loading').classList.remove('hidden');
        document.getElementById('movies-grid').innerHTML = '';
        document.getElementById('error-msg').classList.add('hidden');
    }

    try {
        const res  = await fetch(`${API}/movies/genre/${encodeURIComponent(genre)}?page=${page}`);
        const data = await res.json();
        if (!res.ok) throw new Error('Failed to load movies');

        document.getElementById('loading').classList.add('hidden');
        renderMovies(data);

        document.getElementById('load-more-wrap').classList.toggle('hidden', data.length < 8);

    } catch (err) {
        document.getElementById('loading').classList.add('hidden');
        const errEl = document.getElementById('error-msg');
        errEl.textContent = '⚠️ Could not load movies. Check your OMDb API key in application.properties.';
        errEl.classList.remove('hidden');
    }
}

function renderMovies(movies) {
    const grid = document.getElementById('movies-grid');

    movies.forEach((m, i) => {
        const card = document.createElement('div');
        card.className = 'movie-card';
        card.style.animationDelay = `${i * 0.06}s`;

        const poster = m.posterUrl
            ? `<img class="movie-poster" src="${m.posterUrl}" alt="${escapeHtml(m.title)}" loading="lazy"
                    onerror="this.style.display='none'; this.nextElementSibling.style.display='flex';">
               <div class="movie-poster-placeholder" style="display:none;">🎬</div>`
            : `<div class="movie-poster-placeholder">🎬</div>`;

        const year     = m.releaseDate || 'N/A';
        const rating   = m.rating ? parseFloat(m.rating).toFixed(1) : 'N/A';
        const flag     = m.langFlag || '🌍';
        const lang     = m.language ? m.language.split(',')[0].trim() : '';
        const runtime  = m.runtime && m.runtime !== 'N/A'
            ? `<span>${m.runtime}</span>` : '';
        const director = m.director && m.director !== 'N/A'
            ? `<div style="font-size:0.74rem;color:var(--muted);margin-top:4px;">🎬 ${escapeHtml(m.director)}</div>`
            : '';

        card.innerHTML = `
            ${poster}
            <div class="movie-info">
                <div class="movie-title">${escapeHtml(m.title)}</div>
                <div class="movie-meta">
                    <span class="rating">⭐ ${rating}</span>
                    <span>${year}</span>
                    ${runtime}
                    <span title="${escapeHtml(lang)}">${flag}</span>
                </div>
                ${director}
                ${m.overview && m.overview !== 'N/A'
                    ? `<p style="font-size:0.76rem;color:var(--muted);margin-top:8px;line-height:1.5;
                              display:-webkit-box;-webkit-line-clamp:3;-webkit-box-orient:vertical;overflow:hidden;">
                          ${escapeHtml(m.overview)}
                       </p>`
                    : ''}
            </div>`;

        grid.appendChild(card);
    });
}

function loadMoreMovies() {
    currentPage++;
    document.getElementById('load-more-wrap').classList.add('hidden');
    loadMovies(currentGenre, currentPage);
}

function goToResults() {
    window.location.href = 'result.html';
}

function escapeHtml(str) {
    if (!str) return '';
    return str
        .replace(/&/g, '&amp;')
        .replace(/</g, '&lt;')
        .replace(/>/g, '&gt;')
        .replace(/"/g, '&quot;');
}