/**
 * Alters the status field into writing the current status
 * of the book.
 * Example: "null true" -> "Currently reading"
 */
function renderBookStatus() {
    let id = 'status';
    let statusToRead = 'null false';
    let statusReading = 'null true';

    let statusElem = document.getElementsByClassName(id);
    var size = statusElem.length;
    
    for(let i = 0; i < size ; i++) {
        if(statusElem[i].innerHTML === statusToRead) {
            statusElem[i].innerHTML = 'To Read';
        } else if(statusElem[i].innerHTML === statusReading) {
            statusElem[i].innerHTML = 'Currently reading';
        } else {
            var date = statusElem[i].innerHTML.replace(" false", "");
            if(date != null) {
                statusElem[i].innerHTML = "Completed on " + date;
            }
        }
    }
};

window.onload = renderBookStatus();
