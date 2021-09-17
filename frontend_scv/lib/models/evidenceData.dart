//This class is used to model evidence data from the backend.
//This includes the backend id, and hash

enum EvidenceType { UPLOADED, LINKED }

class EvidenceData {
  String hash;
  String id;
  EvidenceType type;
  String owner;

  //In case of an uploaded file
  String fileName;
  String mimeType;

  //In case of a linked file
  String url;

  //Should include an agreementid soon

  ///Generate and instance from data provided by the server
  EvidenceData.fromJSON(Map<String, dynamic> jsn) {
    hash = jsn['EvidenceHash'];
    id = jsn['EvidenceID'];
    String ow = (jsn['EvidenceOwner']['publicWalletID']);
    owner = ow.toLowerCase();
    var specific = jsn['EvidenceSpecificDetail'];

    if (jsn['EvidenceType'] == 'UPLOADED') {
      type = EvidenceType.UPLOADED;
      fileName = specific['OriginalFileName'];
      mimeType = specific['FileMimeType'];
    } else {
      type = EvidenceType.LINKED;
      url = specific['EvidenceURL'];
    }
  }

  EvidenceData();

  ///This is a semi-temporary method until the json is finalised
  EvidenceData.fromString(String data) {
    int col = data.indexOf(':');
    String aType = data.substring(0, col);
    if (aType == 'UPLOADED') {
      type = EvidenceType.UPLOADED;
    } else {
      type = EvidenceType.LINKED;
    }

    String next = data.substring(col + 1);
    col = next.indexOf(',');
    id = next.substring(0, col);
    hash = next.substring(next.indexOf(':'));
  }
}
