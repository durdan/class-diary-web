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
function HCF(x, y) {
    // Returns the highest common factor of a pair of numbers.
    var temp;
    if (x < 0) {
        x *= -1
    }
    if (y < 0) {
        y *= -1
    }
    if (x == y) {
        return x
    }
    while (x != 0) {
        y = y % x;
        temp = x;
        x = y;
        y = temp;
    }
    return y;
}
function newSheet() {
    var a,b,c,d;
    var num = 0, den = 0;
    var questions = "";
    var level = 2;
    var type;
    score = 0;
    locked = false;
    for (var i = 0; i < totalQuestions; i++) {
        while (num == den || den == 1) {
            type = Math.random();
            while (num == 0 || num % den == 0) {
                a = 1 + Math.floor(Math.random() * level);
                b = 1 + a + Math.floor(Math.random() * level);
                c = 1 + Math.floor(Math.random() * level);
                d = 1 + c + Math.floor(Math.random() * level);
                if (type < 0.5) {
                    num = a * d + c * b;
                }
                else {
                    num = a * d - c * b;
                }
                den = b * d;
            }
            var hcf = HCF(num, den);
            num /= hcf;
            den /= hcf;
        }
        answer[i] = num + "/" + den;

        questions += "<tr><td>" + (i + 1) + ".</td>";
        if (type < 0.5) {
            questions += "<td>\\( \\frac{" + a + "}{" + b + "} + \\frac{" + c + "}{" + d + "} = \\)</td>";
        }
        else {
            questions += "<td>\\( \\frac{" + a + "}{" + b + "} - \\frac{" + c + "}{" + d + "} = \\)</td>";
        }
        questions += "<td><input onclick='this.select()' id='" + i + "' size='3' /></td></tr>";
        level += 1;
        num = den = 0;
    }
    document.getElementById('questions').innerHTML = "<table>" + questions + "</table>";
}