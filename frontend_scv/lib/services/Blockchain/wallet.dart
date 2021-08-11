//This class will be used to connect to crypto-wallets, initially only Metamask is supported.
//It is also a singleton.

import 'dart:html';
import 'package:web3dart/browser.dart';
import 'package:web3dart/credentials.dart';
import '../../models/global.dart';

class WalletInteraction {
  CredentialsWithKnownAddress metaCred;
  bool ready = false; //Basic check if the class can be used yet
  static final WalletInteraction _wall = WalletInteraction._internal();

  WalletInteraction._internal();
  factory WalletInteraction() {
    return _wall;
  }

  //Temp string
  Future<void> metamaskConnect() async {
    //For this to work, a chrome session must be started out of debug mode. Copy the url to a normal tab
    final meta = window.ethereum;
    if (!metamaskAvailable()) {
      print('Connection failed');
      throw Exception("Metamask is not available");
    }

    metaCred = await meta.requestAccount();

    Global.userAddress = metaCred.address.toString(); //Save the user address to globals
    ready = true;

    print ('Connection successful: ' + Global.userAddress);
  }

  CredentialsWithKnownAddress getCredentials() {
    //Haven't tested this syntax;
    return ready ? metaCred : throw Exception('Metamask credentials not ready');
  }

  bool metamaskAvailable() {
    //Checks if Metamask is installed
    return (window.ethereum != null);
  }

  void metamaskDisconnect() {
    //Disconnect from Metamask

    metaCred = null;
    ready = false;
  }
}
