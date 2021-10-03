import 'dart:typed_data';
import 'dart:html';

import 'package:flutter/material.dart';
import 'package:mime/mime.dart';

class EvidenceUploadPanel extends StatefulWidget {
  @override
  _EvidenceUploadPanelState createState() => _EvidenceUploadPanelState();
}

class _EvidenceUploadPanelState extends State<EvidenceUploadPanel> {
  Uint8List uploadFile;
  String _uploadMime;
  String message = '';
  _startFilePicker() async {
    InputElement uploadInput = FileUploadInputElement();
    uploadInput.click();

    uploadInput.onChange.listen((event) {
      //read file content as dataUrl
      final files = uploadInput.files;
      if (files.length == 1) {
        final file = files[0];
        FileReader reader = FileReader();

        reader.onLoadEnd.listen((e) {
          setState(() {
            uploadFile = reader.result;
          });
        });

        reader.onError.listen((fileEvent) {
          setState(() {
            message = "Some Error occured while reading the file";
          });
        });
        reader.readAsArrayBuffer(file);
      }
    });
  }

  @override
  Widget build(BuildContext context) {
    if (uploadFile != null) {
      _uploadMime = lookupMimeType('', headerBytes: uploadFile);
    }

    return Column(
      crossAxisAlignment: CrossAxisAlignment.center,
      children: [
        Text('Upload Evidence:'),
        ElevatedButton(onPressed: _startFilePicker, child: Text('Upload File')),
      ],
    );
  }
}
