//This models an item of evidence that a user may send.
//An evidence, if you will.

import 'dart:io';
import 'package:http/http.dart';

class Evidence {

  //The party that owns the evidence
  String owningParty;
  MultipartFile evidenceFile;
  //More members may be implemented soon

  Future<void> setFile(File f) async {
    evidenceFile = await MultipartFile('UploadFile', f.readAsBytes().asStream(), f.lengthSync());

  }

}
