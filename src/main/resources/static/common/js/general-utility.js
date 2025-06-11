const status = {
    OK: 200,
    NO_CONTENT: 204,
    WARNING: 299,
    BAD_REQUEST: 400,
    UNAUTHORIZED: 401,
    NOT_FOUND: 404,
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
    NOT_FOUND: 'bg-red-600',
    GONE: 'bg-red-600',
    SESSION_TIMEOUT: 'bg-yellow-400',
    INTERNAL_SERVER_ERROR: 'bg-red-600'
};

const categoryLabels = {
    'ICTE': 'ICT Electives',
    'TE': 'Technical Electives',
    'SE': 'Science Electives',
    'MNCE': 'MNCE Electives',
    'OE': 'Open Electives',
    'HSSE': 'Humanities and Social Sciences Electives'
};

const customColors = {
    DARK_BLUE: '#1321EA',
    LIGHT_BLUE: '#ACCEFF',
    COBALT_BLUE: '#1E3C72',
    DARK_GREEN: '#2D9D5D'
};

function HandleStudentRoute(url) {
    window.location.href = `/student/${url}`;
}

function HandleAdminRoute(url) {
    window.location.href = `/admin/${url}`;
}