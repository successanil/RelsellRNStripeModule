/**
 * Sample React Native App
 * https://github.com/facebook/react-native
 *
 * @format
 * @flow
 */

import React, {Component} from 'react';
import {Platform, View, NativeModules, Button} from 'react-native';

class App extends Component {
  render() {
    return (
      <View
        style={{
          height: '100%',
          alignItems: 'center',
          justifyContent: 'center',
        }}>
        <View style={{marginTop: 16}}>
          <Button
            style={{width: 300}}
            onPress={() => launchStripeActivityTwo()}
            title="ANDROID STRIPE"
          />
        </View>
      </View>
    );
  }
}

const launchStripeActivityOne = () => {
  if (Platform.OS === 'android') {
    var videoLoaded = NativeModules.ActivityStarter.navigateToExample();
    videoLoaded
      .then(result => {
        console.log('Promise Returned Success');
      })
      .catch(error => {
        console.log('Promise Returned Failure');
      });
  }
};

const launchStripeActivityTwo = () => {
  if (Platform.OS === 'android') {
    var paymentConfigFromRN = {
      amount: 15.0,
      debug: true,
      isLiveModeStripeUserId: false,
      paymentUrl: 'http://www.relsellglobal.co.in/payment',
      testpayments:true
    };
    var videoLoaded = NativeModules.ActivityStarter.navigateToStripeMain(
      JSON.stringify(paymentConfigFromRN),
    );
    videoLoaded
      .then(result => {
        console.log('Promise Returned Success');
      })
      .catch(error => {
        console.log('Promise Returned Failure');
      });
  }
};

export default App;
