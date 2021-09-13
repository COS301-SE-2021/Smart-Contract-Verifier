//This widget displays a list of all evidence of an agreement.
//Clicking on evidence should display the actual file.

import 'package:flutter/material.dart';
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

  EvidenceService evServe = EvidenceService();

  @override
  Widget build(BuildContext context) {
    return Scaffold(
      body: Container(

        child: FutureBuilder(
            future: evServe.getEvidenceData(widget.agreementId), builder: (context, snapshot) {
              List<EvidenceDisplayItem> cards = [];
              if (snapshot.connectionState != ConnectionState.done) {
               return CircularProgressIndicator();
              }

              List<EvidenceData> evs = snapshot.data;
              for (int i =0;i<evs.length;i++) {
                // print ('Reading ' + i.toString());
                cards.add(EvidenceDisplayItem(evs[i]));
              }
              return Column(children: cards,);
        }),
      ),
    );
  }

}