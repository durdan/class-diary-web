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
    var a, n, a2, n2, first, second;
    var questions = "";
    var question = "";
    var level = 3;
    score = 0;
    locked = false;
    for (var i = 0; i < totalQuestions; i++) {
        a = (2 + Math.floor(Math.random() * 18)) / 2;
        a2 = (2 + Math.floor(Math.random() * 18)) / 2;
        n = Math.floor((-level / 2 + Math.random() * level));
        n2 = n + Math.floor(-level / 2 + Math.random() * level);
        if (i < 4) {
            while (n < 1 || n2 < 1) {
                n = Math.floor((-level + Math.random() * 2 * level));
                n2 = n + Math.floor(-level + Math.random() * 2 * level);
            }
        }
        first = a * Math.pow(10, n);
        second = a2 * Math.pow(10, n2);
        if (Math.random() < 0.5 || i < 3) {
            question = "\\((" + a + " &#215; 10^{" + n + "}) + (" + a2 + " &#215; 10^{" + n2 + "})\\)";
            answer[i] = first + second;
        }
        else {
            question = "\\((" + a + " &#215; 10^{" + n + "}) - (" + a2 + " &#215; 10^{" + n2 + "})\\)";
            answer[i] = first - second;
        }
        answer[i] = Math.round(answer[i] * 1000000) / 1000000;
        level += 0.2;
        questions += "<tr><td>" + (i + 1) + ".</td>";
        questions += "<td>" + question + "</td>";
        questions += "<td class='answerGap'><input onclick='this.select()' id='" + i + "' size='8' /></td></tr>";
    }
    document.getElementById('questions').innerHTML = "<table>" + questions + "</table>";
}