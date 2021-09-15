//This class will be used to connect to crypto-wallets, initially only Metamask is supported.
//It is also a singleton.

import 'dart:html';
import 'dart:html';
import 'dart:js_util';
import 'dart:typed_data';

import 'package:unison/services/Blockchain/smartContract.dart';
import 'package:unison/services/External/jsMethods.dart';
import 'package:web3dart/browser.dart';
import 'package:web3dart/credentials.dart';
import 'package:web3dart/web3dart.dart';
//import 'dart:js' as js;
import 'package:js/js.dart';
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
    // print ('Before');
    // var r = meta.asRpcService();
    // await r.call('wallet_watchAsset', [{'type':"ERC20",
    //   "options" : { "address": "0x020184394bD9332609AC9992CB1208EB5e1B189e",
    //   "symbol": "UNT",
    //   "decimals": 18,
    //   "image" : 'http://placekitten.com/200/300'}}]);


    Global.userAddress =
        metaCred.address.toString(); //Save the user address to globals
    ready = true;

    print('Connection successful: ' + Global.userAddress);
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

  ///Sign a string using metamask personal signing
  Future<Uint8List> personalSign(String data) async {

    //Sign using external Javascript function, since the Dart Metamask signing is unreliable.
    var res = await promiseToFuture(signWithMetamask(data, Global.userAddress));
    return res;
  }


}
