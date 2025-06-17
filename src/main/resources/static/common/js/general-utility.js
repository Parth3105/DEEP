const status = {
    OK: 200,
    NO_CONTENT: 204,
    WARNING: 299,
    BAD_REQUEST: 400,
    UNAUTHORIZED: 401,
    FORBIDDEN: 403,
    NOT_FOUND: 404,
    CONFLICT: 409,
    GONE: 410,
    SESSION_TIMEOUT: 419,
    INTERNAL_SERVER_ERROR: 500
};

const statusColors = {
    OK: 'bg-green-600',
    NO_CONTENT: 'bg-yellow-400',
    WARNING: 'bg-yellow-400',
    BAD_REQUEST: 'bg-yellow-400',
    UNAUTHORIZED: 'bg-red-600',
    FORBIDDEN: 'bg-red-600',
    NOT_FOUND: 'bg-red-600',
    CONFLICT: 'bg-red-600',
    GONE: 'bg-red-600',
    SESSION_TIMEOUT: 'bg-yellow-400',
    INTERNAL_SERVER_ERROR: 'bg-red-600',
    ERROR: 'bg-red-600'
};

const categoryLabels = {
    'ICTE': 'ICT Electives',
    'TE': 'Technical Electives',
    'SE': 'Science Electives',
    'MNCE': 'MNCE Electives',
    'OE': 'Open Electives',
    'HASSE': 'Humanities and Social Sciences Electives'
};

function HandleStudentRoute(url) {
    const contextPath = document.querySelector('meta[name="context-path"]').getAttribute('content');
    window.location.href = `${contextPath}student/${url}`;
}

function HandleAdminRoute(url) {
    const contextPath = document.querySelector('meta[name="context-path"]').getAttribute('content');
    window.location.href = `${contextPath}admin/${url}`;
}