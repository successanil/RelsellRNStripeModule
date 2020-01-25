const express = require('express')
const payment = require('./payment')
var bodyParser = require("body-parser");


const app = express()
const port = 3000

app.use(bodyParser.json());

app.post('/payment',(req,res)=>{
    var p = payment.processPayment(req,res);
    p
    .then((result)=>{
    //    console.log(result);
       return res.send({data:result});
    })
    .catch(err=>{
       return res.send('Err '+err);
    })

    
});

app.listen(port, () => console.log(`Example app listening on port ${port}!`))