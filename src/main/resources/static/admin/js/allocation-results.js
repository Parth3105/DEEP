if(renderResponse) {
    printStatusResponse(renderResponse);
}

function submitWithPath(event) {
    event.preventDefault();
    const sid = document.getElementById("studentId").value.trim();
    if (sid) {
        window.location.href = `/admin/allocation-results/${encodeURIComponent(sid)}`;
    }
}
