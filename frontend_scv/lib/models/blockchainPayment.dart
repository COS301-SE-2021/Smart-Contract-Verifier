//This class describes payment information for an agreement on the blockchain.
//Though Verifier supports it, it is here assumed that only one
// payment condition exists for an agreement.

class BlockChainPayment {

  String payingUser;
  String payedUser;
  BigInt amount;
  bool hasBeenPayed;

  BlockChainPayment.fromArray(List<dynamic> data) {
    //data[0] is the address of the coin
    amount = data[1];
    payingUser = data[2];
    payedUser = data[3];
    hasBeenPayed = data[4];
  }

}