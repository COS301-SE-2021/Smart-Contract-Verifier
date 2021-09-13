//This widget displays a list of all evidence of an agreement.
//Clicking on evidence should display the actual file.

import 'package:awesome_loader/awesome_loader.dart';
import 'package:file_picker/file_picker.dart';
import 'package:flutter/material.dart';
import 'package:unison/models/evidence.dart';
import 'package:unison/models/evidenceData.dart';
import 'package:unison/services/Server/evidenceService.dart';

import 'EvidenceDisplayItem.dart';

class EvidenceListPanel extends StatefulWidget {

  final String agreementId;
  EvidenceListPanel(this.agreementId);

  @override
  _EvidenceListPanelState createState() => _EvidenceListPanelState();

}

class _EvidenceListPanelState extends State<EvidenceListPanel> {

  TextEditingController _urlLinkController = new TextEditingController();
  EvidenceService evServe = EvidenceService();

  Future<void> uploadEvidence() async {
      FilePickerResult picked = await FilePicker.platform.pickFiles();

      List<PlatformFile> fileRes = picked.files;
      Evidence evid = Evidence(widget.agreementId);
      await evid.setFile(fileRes[0]); //May be expanded in the future to allow multiple files

      await evServe.storeEvidence(evid);
      setState(() {
        build(context);
      });
  }

  Future<void> linkEvidence() async {
    await showDialog(
        context: context,
        builder: (context) {
          return AlertDialog(
            title: Text('Link evidence'),
            actions: [
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
                  await evServe.linkEvidence(_urlLinkController.text, widget.agreementId);

                  _urlLinkController.clear();
                  Navigator.of(context).pop();
                  setState(() {
                    build(context);
                  });
                },
              ),
            ],
            content: TextFormField(
              validator: (value) {
                if (value.isEmpty) {
                  return 'Url:';
                } else {
                  return null;
                }
              },
              decoration: InputDecoration(labelText: 'Evidence Url'),
              controller: _urlLinkController,
            ),
          );
        });
  }

  @override
  Widget build(BuildContext context) {
    return Scaffold(
      body: Container(

        child: FutureBuilder(
            future: evServe.getEvidenceData(widget.agreementId), builder: (context, snapshot) {
              List<Widget> cards = [Center(
                child: Row(mainAxisAlignment: MainAxisAlignment.center,
                  children: [
                    Center(child: TextButton(child: Text('Upload Evidence to UniServer(TM)'), onPressed: uploadEvidence,)),
                    SizedBox(width: 100),
                    Center(child: TextButton(child: Text('Provide a link to evidence (url)'), onPressed: linkEvidence,)),
                  ],
                ),
              )];
              if (snapshot.connectionState != ConnectionState.done) {
               return AwesomeLoader();
              }

              List<EvidenceData> evs = snapshot.data;
              for (int i =0;i<evs.length;i++) {
                // print ('Reading ' + i.toString());
                cards.add(SizedBox(height: 10,));
                cards.add(EvidenceDisplayItem(evs[i], widget.agreementId));
              }
              return SingleChildScrollView(child: Column(children: cards,));
        }),
      ),
    );
  }

}