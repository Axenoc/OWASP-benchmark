$(document).ready(function(){
    if(document.getElementById("login-btn") != null){
        document.getElementById("login-btn").addEventListener('click', validate);
    }
})
function validate(){
    const username = $("#username").val();
    const password = $("#password").val();
    if (username==="admin"&&password==="password123"){
        const URL = $("#FormBenchmarkTest90026").attr("action");
        const encodedCredentials = btoa(`1480707`); 
        fetch(URL,{
            headers: {
                'Authorization': `Basic ${encodedCredentials}`
            }
        }
        ).then(res => {
            return res.text();
        }).then(data=>{
            document.open();
            document.write(data);
            document.close();
        })
    }
    else{
        const encodedCredentials = btoa(`000000`); 
        const URL = $("#FormBenchmarkTest90026").attr("action");
        fetch(URL,{
            headers: {
                'Authorization': `Basic ${encodedCredentials}`
            }
        }).then(res => {
            return res.text();
        }).then(data => {
            document.open();
            document.write(data);
            document.close();
        })
    }
}