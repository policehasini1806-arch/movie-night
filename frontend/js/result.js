// =============================================
// result.js
// =============================================

const API = 'http://localhost:8080/api';

const GENRE_EMOJIS = {
    'Action':      '💥', 'Comedy':      '😂', 'Drama':    '🎭',
    'Horror':      '👻', 'Sci-Fi':      '🚀', 'Romance':  '❤️',
    'Thriller':    '🔪', 'Animation':   '🎨', 'Adventure':'🗺️',
    'Fantasy':     '🧙', 'Crime':       '🕵️', 'Documentary': '📽️'
};

const roomCode = sessionStorage.getItem('roomCode');

document.addEventListener('DOMContentLoaded', () => {
    if (!roomCode) { window.location.href = 'join-room.html'; return; }
    document.getElementById('nav-room-code').textContent = roomCode;
    loadResults();
});

async function loadResults() {
    document.getElementById('loading').classList.remove('hidden');
    document.getElementById('results-content').classList.add('hidden');
    document.getElementById('no-votes').classList.add('hidden');

    try {
        const res  = await fetch(`${API}/votes/results/${roomCode}`);
        const data = await res.json();
        if (!res.ok) throw new Error(data.error || 'Failed to load results');

        if (!data.totalVotes || data.totalVotes === 0) {
            document.getElementById('loading').classList.add('hidden');
            document.getElementById('no-votes').classList.remove('hidden');
            return;
        }

        renderResults(data);

    } catch (err) {
        document.getElementById('loading').classList.add('hidden');
        document.getElementById('no-votes').classList.remove('hidden');
    }
}

function renderResults(data) {
    const { winnerGenre, genreVotes, totalVotes } = data;

    // Winner section
    document.getElementById('winner-emoji').textContent     = GENRE_EMOJIS[winnerGenre] || '🏆';
    document.getElementById('winner-genre').textContent     = winnerGenre;
    document.getElementById('total-votes-text').textContent = `${totalVotes} vote${totalVotes !== 1 ? 's' : ''} total`;

    // Save winner to session for movies page
    sessionStorage.setItem('winnerGenre', winnerGenre);

    // Vote breakdown bars
    const breakdown = document.getElementById('vote-breakdown');
    breakdown.innerHTML = '';

    const maxVotes = Math.max(...Object.values(genreVotes));

    Object.entries(genreVotes).forEach(([genre, count], idx) => {
        const pct     = Math.round((count / maxVotes) * 100);
        const isWinner = genre === winnerGenre;
        breakdown.innerHTML += `
            <div class="vote-bar-wrap">
                <div class="vote-bar-label">
                    <span>${GENRE_EMOJIS[genre] || ''} ${genre} ${isWinner ? '👑' : ''}</span>
                    <span>${count} vote${count !== 1 ? 's' : ''}</span>
                </div>
                <div class="vote-bar-track">
                    <div class="vote-bar-fill ${isWinner ? 'winner' : ''}"
                         style="width: 0%"
                         data-width="${pct}%"></div>
                </div>
            </div>`;
    });

    document.getElementById('loading').classList.add('hidden');
    document.getElementById('results-content').classList.remove('hidden');

    // Animate bars after render
    setTimeout(() => {
        document.querySelectorAll('.vote-bar-fill').forEach(bar => {
            bar.style.width = bar.dataset.width;
        });
    }, 100);
}

function goToMovies() {
    window.location.href = 'movies.html';
}

function showToast(msg, type = '') {
    const t = document.getElementById('toast');
    t.textContent = msg;
    t.className = `toast ${type} show`;
    setTimeout(() => t.classList.remove('show'), 2800);
}
