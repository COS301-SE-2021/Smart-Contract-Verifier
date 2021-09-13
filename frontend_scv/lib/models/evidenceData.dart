//This class is used to model evidence data from the backend.
//This includes the backend id, and hash

enum EvidenceType {
  UPLOADED, LINKED
}

class EvidenceData {
  String hash;
  String id;
  EvidenceType type;

  //Should include an agreementid soon

  EvidenceData.fromJSON(Map<String, String> jsn) {

  }

  EvidenceData();

  ///This is a semi-temporary method until the json is finalised
  EvidenceData.fromString(String data) {
    int col = data.indexOf(':');
    String aType = data.substring(0, col);
    if (aType == 'UPLOADED') {
      type = EvidenceType.UPLOADED;
    }
    else {
      type = EvidenceType.LINKED;
    }

    String next = data.substring(col +1);
    col = next.indexOf(',');
    id = next.substring(0, col);
    hash = next.substring(next.indexOf(':'));

  }

}