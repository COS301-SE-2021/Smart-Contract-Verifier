//This class will be used to connect to crypto-wallets, initially only Metamask is supported.

import 'dart:html';
import 'package:web3dart/browser.dart';
import 'package:web3dart/credentials.dart';
import '../../providers/global.dart';
// import 'package:http/http.dart';
// import 'package:web3dart/web3dart.dart';

class WalletInteraction {

  CredentialsWithKnownAddress metaCred;
  bool ready = false; //Basic check if the class can be used yet

  Future<void> metamaskConnect() async { //For this to work, a chrome session must be started out of debug mode. Copy the url to a normal tab
    final meta = window.ethereum;
    if (meta == null) {
      print ('Connection failed');
      throw Exception("Metamask is not available");
    }

    metaCred = await meta.requestAccount();
    Global.userAddress = metaCred.address.toString(); //Save the user address to globals
    ready = true;
    print ('Connection successful');
  }

  CredentialsWithKnownAddress getCredentials() {

    //Haven't tested this syntax;
    return ready ? metaCred : throw Exception('Metamask credentials not ready');
  }


}