/**
 * Sample React Native App
 * https://github.com/facebook/react-native
 *
 * @format
 * @flow
 */

import React, {Component} from 'react';
import {View, NativeModules, Button} from 'react-native';

class App extends Component {
  render() {
    return (
      <View>
        <Button
          onPress={() => navigateToExample()}
          title="Start example activity"
        />
      </View>
    );
  }
}

const navigateToExample = () => {
  var videoLoaded = NativeModules.ActivityStarter.navigateToExample();
  videoLoaded
    .then(result => {
      console.log('Promise Returned Success');
    })
    .catch(error => {
      console.log('Promise Returned Failure');
    });
};

export default App;
