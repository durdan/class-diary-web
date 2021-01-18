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
    var a, b, c, d, e, f;
    var num = 0, den = 0;
    var questions = "";
    var level = 2;
    var type;
    score = 0;
    locked = false;
    for (var i = 0; i < totalQuestions; i++) {
        type = Math.random();
        while (num == den || den == 1 || num == - den) {
            while (num == 0 || num % den == 0) {
                a = 1 + Math.floor(Math.random() * level);
                b = 1 + a + Math.floor(Math.random() * level);
                c = 1 + Math.floor(Math.random() * level);
                d = 1 + c + Math.floor(Math.random() * level);
                e = 1 + Math.floor(Math.random() * level);
                f = 1 + Math.floor(Math.random() * level);
                if (type < 0.5) {
                    num = (d * (e * b + a)) + (b * (f * d + c));
                }
                else {
                    num = (d * (e * b + a)) - (b * (f * d + c));
                }
                den = b * d;
            }
            for (var j = 2; j < Math.max(Math.abs(num), den); j++) {
                if (num % j == 0 && den % j == 0) {
                    num /= j;
                    den /= j;
                    j -= 1;
                }
            }

        }
        answer[i] = num + "/" + den;
        num = den = 0;
        questions += "<tr><td>" + (i + 1) + ".</td>";
        if (type < 0.5) {
            questions += "<td>\\(" + e + "\\frac{" + a + "}{" + b + "} + " + f + "\\frac{" + c + "}{" + d + "} = \\)</td>";
        }
        else {
            questions += "<td>\\(" + e + "\\frac{" + a + "}{" + b + "} - " + f + "\\frac{" + c + "}{" + d + "} = \\)</td>";
        }
        questions += "<td><input onclick='this.select()' id='" + i + "' size='3' /></td></tr>";
        level += 0.5;
    }
    document.getElementById('questions').innerHTML = "<table>" + questions + "</table>";
}