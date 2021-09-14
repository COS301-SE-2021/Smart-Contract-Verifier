//This service is used to interact with the faucet smart contract,
//in order to test on the Mumbai testnet

import 'package:unison/services/Blockchain/smartContract.dart';

class FaucetService {
  SmartContract _smC = SmartContract('JSON/_src_Faucet.abi', 'Faucet');

  Future<void> getToken() async {
    await _smC.makeWriteCall('getToken', []);
  }


}