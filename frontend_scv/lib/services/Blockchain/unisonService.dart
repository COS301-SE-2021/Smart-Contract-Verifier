//This class will be used to interact with the Unison smart Contract to save and get contracts from the blockchain

import 'package:web3dart/credentials.dart';

import '../../providers/contract.dart';
import 'smartContract.dart';
import '../../providers/global.dart';

class UnisonService {

  SmartContract _smC = SmartContract();

  Future<void> saveAgreement(Contract con) async {
    //Todo list:
    //Use relevant function name to call smart contract
    //Use con to construct parameters
    //In a real deployed contract, what will the delay be?
    //When will a result be returned?
    //How should it be handled in the UI?

    //Sort out events, and detecting them.

    var jsn = con.toJsonChain();

    String data = con.title + '#' + con.description;
    String partyB = (Global.userAddress == jsn['PartyB'])? jsn['PartyA'] : jsn['PartyB'];
    EthereumAddress partyBA = EthereumAddress.fromHex(partyB);
    print ('A: '+Global.userAddress);


    final res = await _smC.makeWriteCall("createAgreement", [partyBA, con.duration, data]); //Soon, this will be replaced bu a spinner in the UI
    // It will have to check for an event.
    print (res);

  }

  Future<void> acceptAgreement(Contract con) async { //This should probably be called immediately after the contract is sealed on backend.
    //TODO list:
    //Use function name acceptAgreement
    //Create parameter list. Only Agreement id. From where?

    if (!con.movedToBlockchain) {
      throw Exception('Agreement is not on blockchain yet');
    }

    final res = await _smC.makeWriteCall('acceptAgreement', [con.blockchainId]);

  }

  Future<void> getAgreement(BigInt id) async {

    final res = await _smC.makeReadCall('getAgreement', [id]);
    print (res);

  }


}