//This file is the service used for interacting with the UnisonToken smart contract.

import 'package:unison/models/global.dart';
import 'package:web3dart/credentials.dart';
import 'smartContract.dart';

class TokenService {

  SmartContract _smC = SmartContract("JSON/_src_UnisonToken_sol_UnisonToken.abi", 'UnisonToken');

  Future<void> setStake(BigInt stake) async { //Let the user stake an amount on UnisonToken.

    try {
     await _smC.makeWriteCall(
          'approve', [EthereumAddress.fromHex(await Global.getContractId('Verifier')), stake]);
    }
    catch (e) {
      print (e);
      return;
    }

  }


}