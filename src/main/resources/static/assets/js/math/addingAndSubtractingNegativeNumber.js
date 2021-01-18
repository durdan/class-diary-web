var totalQuestions = 10;
var score = 0;
var locked = false;
var answer = new Array(totalQuestions);

function showAnswers() {
    locked = true;
    score = 0;
    for (var i = 0; i < totalQuestions; i++) {
        document.getElementById(i).value = answer[i];
        document.getElementById(i).style.background = '#ffbbbb';
        document.getElementById(i).readOnly = true;
    }
}
function markAnswers(){
    score = 0;
    if (!locked) {
        for (i = 0; i < totalQuestions; i++) {
            document.getElementById(i).value = document.getElementById(i).value.replace(/ /g,'');
            if (document.getElementById(i).value == answer[i] && document.getElementById(i).value != "") {
                document.getElementById(i).style.background = '#bbffbb';
                score += 1;
            }
            else {
                document.getElementById(i).style.background = '#ffbbbb';
            }
        }
        document.getElementById('score').value = score;
    }
}
function newSheet() {
    var a,b;
    var chance;
    var questions = "";
    var question = "";
    var level = 8;
    score = 0;
    locked = false;
    for (var i = 0; i < totalQuestions; i++) {
        chance = Math.floor(Math.random()*7);
        if (chance == 0) {
            a = -(1 + Math.floor(Math.random()*(level)))
            b = 1 + Math.floor(Math.random()*(level))
            question = "(-" + Math.abs(a) + ") + " + b;
            answer[i] = a + b;
        }
        if (chance == 1) {
            a = 1 + Math.floor(Math.random()*(level))
            b = -(1 + Math.floor(Math.random()*(level)))
            question = a + " + (-" + Math.abs(b) + ")";
            answer[i] = a + b;
        }
        if (chance == 2) {
            a = -(1 + Math.floor(Math.random()*(level)))
            b = -(1 + Math.floor(Math.random()*(level)))
            question = "(-" + Math.abs(a) + ") + (-" + Math.abs(b) + ")";
            answer[i] = a + b;
        }
        if (chance == 3) {
            a = -(1 + Math.floor(Math.random()*(level)))
            b = 1 + Math.floor(Math.random()*(level))
            question = "(-" + Math.abs(a) + ") - " + b;
            answer[i] = a - b;
        }
        if (chance == 4) {
            a = 1 + Math.floor(Math.random()*(level))
            b = -(1 + Math.floor(Math.random()*(level)))
            question = a + " - (-" + Math.abs(b) + ")";
            answer[i] = a - b;
        }
        if (chance == 5) {
            a = -(1 + Math.floor(Math.random()*(level)))
            b = -(1 + Math.floor(Math.random()*(level)))
            if (a == b) {
                b -= 1
            }
            question = "(-" + Math.abs(a) + ") - (-" + Math.abs(b) + ")";
            answer[i] = a - b;
        }
        if (chance == 6) {
            a = 1 + Math.floor(Math.random()*(level))
            b = a + 1 + Math.floor(Math.random()*(level))
            question = a + " - " + b;
            answer[i] = a - b;
        }
        level *= 1.4;
        questions += "<tr><td>" + (i + 1) + ".</td>";
        questions += "<td>\\(" + question + " = \\)</td>";
        questions += "<td><input onclick='this.select()' id='" + i + "' size='1' /></td></tr>";
    }
    document.getElementById('questions').innerHTML = "<table>" + questions + "</table>";
}