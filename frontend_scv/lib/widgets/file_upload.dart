import 'package:flutter/material.dart';
import 'package:file_picker/file_picker.dart';

class FileUpload extends StatelessWidget {
  @override
  Widget build(BuildContext context) {
    return OutlinedButton(
      child: Text('UPLOAD FILE'),
      onPressed: () async {
        var picked = await FilePicker.platform.pickFiles();

        if (picked != null) {
          var fileRaw = picked.files.first.bytes;
          print(fileRaw);
        }
      },
    );
  }
}
