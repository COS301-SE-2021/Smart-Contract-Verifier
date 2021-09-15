import 'package:file_picker/file_picker.dart';
import 'package:flutter/material.dart';
import 'package:unison/models/evidence.dart';
import 'package:unison/services/Server/evidenceService.dart';

class UploadEvidencePanel extends StatefulWidget {
  final String agreementId;
  final Function rebuildScreen;
  UploadEvidencePanel(this.agreementId, this.rebuildScreen);

  @override
  _UploadEvidencePanelState createState() => _UploadEvidencePanelState();
}

class _UploadEvidencePanelState extends State<UploadEvidencePanel> {
  TextEditingController _urlLinkController = new TextEditingController();
  EvidenceService evServe = EvidenceService();

  GlobalKey<FormState> _formKey = GlobalKey<FormState>();
  Future<void> uploadEvidence() async {
    FilePickerResult picked =
        await FilePicker.platform.pickFiles(type: FileType.image);

    List<PlatformFile> fileRes = picked.files;
    Evidence evid = Evidence(widget.agreementId);
    await evid.setFile(
        fileRes[0]); //May be expanded in the future to allow multiple files

    await evServe.storeEvidence(evid);
    widget.rebuildScreen();
  }

  Future<void> linkEvidence() async {
    await showDialog(
      context: context,
      builder: (context) {
        return AlertDialog(
          title: Text('Link evidence'),
          content: Form(
            key: _formKey,
            child: Column(
              mainAxisSize: MainAxisSize.min,
              children: [
                TextFormField(
                  validator: (value) {
                    if (value.isEmpty) {
                      return 'Please enter a URL';
                    } else {
                      bool _validURL = Uri.parse(value).isAbsolute;
                      if (!_validURL) {
                        return 'Please enter a value/absolute URL';
                      }
                      return null;
                    }
                  },
                  decoration: InputDecoration(labelText: 'Evidence Url'),
                  controller: _urlLinkController,
                ),
              ],
            ),
          ),
          actions: <Widget>[
            TextButton(
              child: Text('Cancel'),
              onPressed: () {
                _urlLinkController.clear();
                Navigator.of(context).pop();
              },
            ),
            TextButton(
              child: Text('Save'),
              onPressed: () async {
                final isValid = _formKey.currentState.validate();
                if (!isValid) return;
                await evServe.linkEvidence(
                    _urlLinkController.text, widget.agreementId);
                _urlLinkController.clear();
                Navigator.of(context).pop();
                widget.rebuildScreen();
              },
            ),
          ],
        );
      },
    );
  }

  @override
  Widget build(BuildContext context) {
    return Column(
      mainAxisAlignment: MainAxisAlignment.end,
      crossAxisAlignment: CrossAxisAlignment.end,
      children: [
        FloatingActionButton.extended(
          onPressed: uploadEvidence,
          label: Text('Upload Image'),
          icon: Icon(Icons.file_upload_outlined),
          backgroundColor: Color.fromRGBO(50, 183, 196, 1),
        ),
        SizedBox(
          height: 15,
        ),
        FloatingActionButton.extended(
          onPressed: linkEvidence,
          label: Text('Share Link'),
          icon: Icon(Icons.add_link_outlined),
          backgroundColor: Color.fromRGBO(50, 183, 196, 1),
        ),
      ],
    );
  }
}
