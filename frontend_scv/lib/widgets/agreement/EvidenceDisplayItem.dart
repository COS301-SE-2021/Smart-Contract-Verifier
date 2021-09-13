//This is an item to be displayed in a list.
//Should be clickable to display the actual evidence file

import 'package:awesome_loader/awesome_loader.dart';
import 'package:flutter/cupertino.dart';
import 'package:flutter/material.dart';
import 'package:unison/models/evidence.dart';
import 'package:unison/models/evidenceData.dart';
import 'package:unison/services/Server/evidenceService.dart';

class EvidenceDisplayItem extends StatefulWidget {
  final EvidenceData metaData;
  final String agreementID;

  EvidenceDisplayItem(this.metaData, this.agreementID);

  @override
  _EvidenceDisplayItemState createState() => _EvidenceDisplayItemState();
}

class _EvidenceDisplayItemState extends State<EvidenceDisplayItem> {
  bool displayItem = false;
  EvidenceService evs = EvidenceService();

  @override
  Widget build(BuildContext context) {
    return displayItem
        ? Center( //File is displayed
            child: Column(
              children: [
                ListTile(
                  title: Text('Evidence ID: ' + widget.metaData.id),
                  tileColor: Color.fromRGBO(102, 10, 102, 1.0),
                  shape: RoundedRectangleBorder(
                    borderRadius: BorderRadius.circular(10),
                  ),
                ),
                Container(
                  margin: EdgeInsets.symmetric(vertical: 15, horizontal: 10),
                    child: FutureBuilder(
                        future: evs.getEvidenceU(
                            widget.metaData, widget.agreementID),
                        builder: (context, snap) {

                          if (snap.connectionState != ConnectionState.done)
                            return AwesomeLoader();

                          Evidence ev = snap.data;
                          Image ret = Image.memory(ev.baseFile.bytes);
                          return SizedBox(child: ret, height: 500,);
                          //return ret;
                        }))
              ],
            ),
          )
        : GestureDetector( //File is not displayed
            onTap: () {
              print("Tapped....");
              displayItem = true;
              setState(() {
                build(context);
              });
            },
            child: Center(
              child: ListTile(
                title: Text('Evidence ID: ' + widget.metaData.id),
                tileColor: Color.fromRGBO(102, 10, 102, 1.0),
                shape: RoundedRectangleBorder(
                  borderRadius: BorderRadius.circular(10),
                ),
              ),
            ),
          );
  }
}