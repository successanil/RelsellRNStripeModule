const stripe = require('stripe')('sk_test_bTgqLNH0d3BS3cUQYXmoZYAq');

var processPayment = (req, res) => {
    if(req.body.testpayments === true){
       console.log('test payments'); 
    }
  return new Promise((resolve, reject) => {
      
    (async () => {
      const charge = await stripe.charges.create({
        amount: 1000,
        currency: 'INR',
        source: 'tok_visa',
        description: 'Example charge',
      });
      if (charge) {
        //   console.log(charge);
        resolve(charge);
      } else {
        reject(charge);
      }
    })();
  });
};

module.exports = {
  processPayment,
};
