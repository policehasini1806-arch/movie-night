// =============================================
// vote.js
// =============================================

const API = 'http://localhost:8080/api';

const GENRES = [
    { name: 'Action',      emoji: '💥' },
    { name: 'Comedy',      emoji: '😂' },
    { name: 'Drama',       emoji: '🎭' },
    { name: 'Horror',      emoji: '👻' },
    { name: 'Sci-Fi',      emoji: '🚀' },
    { name: 'Romance',     emoji: '❤️' },
    { name: 'Thriller',    emoji: '🔪' },
    { name: 'Animation',   emoji: '🎨' },
    { name: 'Adventure',   emoji: '🗺️' },
    { name: 'Fantasy',     emoji: '🧙' },
    { name: 'Crime',       emoji: '🕵️' },
    { name: 'Documentary', emoji: '📽️' }
];

let selectedGenre = null;
const participantId   = sessionStorage.getItem('participantId');
const participantName = sessionStorage.getItem('participantName');
const roomCode        = sessionStorage.getItem('roomCode');
const roomName        = sessionStorage.getItem('roomName');

document.addEventListener('DOMContentLoaded', async () => {
    // Redirect if no session
    if (!participantId || !roomCode) {
        window.location.href = 'join-room.html';
        return;
    }

    // Set nav / header info
    document.getElementById('nav-room-code').textContent = roomCode;
    document.getElementById('voter-name').textContent    = participantName;
    document.getElementById('room-name').textContent     = roomName;

    // Check if already voted
    const votedRes = await fetch(`${API}/votes/check/${participantId}/${roomCode}`);
    const votedData = await votedRes.json();
    if (votedData.hasVoted) {
        document.getElementById('vote-form').classList.add('hidden');
        document.getElementById('already-voted').classList.remove('hidden');
        return;
    }

    // Build genre grid
    buildGenreGrid();
});

function buildGenreGrid() {
    const grid = document.getElementById('genre-grid');
    grid.innerHTML = '';
    GENRES.forEach(g => {
        const btn = document.createElement('button');
        btn.className = 'genre-btn';
        btn.dataset.genre = g.name;
        btn.innerHTML = `<span class="emoji">${g.emoji}</span>${g.name}`;
        btn.onclick = () => selectGenre(g.name, btn);
        grid.appendChild(btn);
    });
}

function selectGenre(genre, btnEl) {
    // Deselect all
    document.querySelectorAll('.genre-btn').forEach(b => b.classList.remove('selected'));
    btnEl.classList.add('selected');
    selectedGenre = genre;

    // Show selected display
    document.getElementById('selected-genre-name').textContent = genre;
    document.getElementById('selected-genre-display').classList.remove('hidden');
    document.getElementById('voteBtn').disabled = false;
}

async function submitVote() {
    if (!selectedGenre) return;

    const btn = document.getElementById('voteBtn');
    btn.disabled = true;
    btn.textContent = 'Submitting...';

    try {
        const res = await fetch(`${API}/votes/submit`, {
            method: 'POST',
            headers: { 'Content-Type': 'application/json' },
            body: JSON.stringify({
                participantId: parseInt(participantId),
                roomCode,
                genre: selectedGenre
            })
        });

        const data = await res.json();
        if (!res.ok) throw new Error(data.error || 'Failed to submit vote');

        showToast('Vote submitted! 🗳️', 'success');
        setTimeout(() => window.location.href = 'result.html', 1000);

    } catch (err) {
        showToast(err.message, 'error');
        btn.disabled = false;
        btn.textContent = '🗳️ Submit Vote';
    }
}

function goToResults() {
    window.location.href = 'result.html';
}

function showToast(msg, type = '') {
    const t = document.getElementById('toast');
    t.textContent = msg;
    t.className = `toast ${type} show`;
    setTimeout(() => t.classList.remove('show'), 2800);
}
