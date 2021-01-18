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
function isPrime(n) {
    if (n < 2) {
        return false
    }
    if (n != Math.round(n)) {
        return false
    }
    var isPrime = true;
    for (var i = 2; i <= Math.sqrt(n); i++) {
        if (n % i == 0) {
            isPrime = false
        }
    }
    return isPrime;
}
function newSheet() {
    var hcf, first, second;
    var questions = "";
    var question = "";
    var level = 5;
    score = 0;
    locked = false;
    for (var i = 0; i < totalQuestions; i++) {
        first = second = 0;
        hcf = 1 + Math.floor(Math.random() * level);
        while (!isPrime(first) || !isPrime(second) || first == second) {
            first = 1 + Math.floor(Math.random() * level);
            second = 1 + Math.floor(Math.random() * level);
        }
        question = "Find the highest common factor of " + (first * hcf) + " and " + (second * hcf) + ".";
        answer[i] = hcf;
        level += 2;
        questions += "<tr><td>" + (i + 1) + ".</td>";
        questions += "<td class='worded'>" + question + "</td>";
        questions += "<td class='answerGap'><input onclick='this.select()' id='" + i + "' size='4' /></td></tr>";
    }
    document.getElementById('questions').innerHTML = "<table>" + questions + "</table>";
}