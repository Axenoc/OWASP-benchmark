$(document).ready(function(){
    if(document.getElementById("login-btn") != null){
        document.getElementById("login-btn").addEventListener('click', sendLogin);
    }
});

function sendLogin(event){
    const id = event.target.id;
    const button = document.getElementById(id);
    const testcase = button.getAttribute("testcase");
    const formVar = `#Form${testcase}`; 
    console.log(formVar);
    var URL = $(formVar).attr("action");
    const username = $("#username").val();
    const password = $("#password").val();
    const encodedCredentials = btoa(`${username}:${password}`); 
    fetch(URL, {
        method: "POST",
        headers: {
            'Authorization': `Basic ${encodedCredentials}` 
        }
    }).then(res => {    
        if (!res.ok){
            return res.status;  
        }
        else{
            return res.text();
        }
    }).then(data => {
        document.open();
        document.write(data);
        document.close();
    });
}