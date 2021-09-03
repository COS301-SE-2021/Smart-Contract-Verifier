//This is the UI counterpart of Contact from models.

import 'package:flutter/material.dart';
import 'package:provider/provider.dart';
import 'package:jdenticon_dart/jdenticon_dart.dart';
import 'package:flutter_svg/flutter_svg.dart';
import 'package:unison/models/contact.dart';
import 'package:unison/services/Server/contactService.dart';

import '../models/contract.dart';
import '../providers/auth.dart';
import '../screens/view_contract_screen.dart';

//Be aware of confusion with Contract (with an r in the middle)
class ContactItem extends StatefulWidget {


  ContactItem(Contact c, String listId) {
    address = c.address;
    alias = c.alias;
    list = listId;
  }

  String address;
  String alias;
  String list; //Which list it is saved in

  @override
  _ContactItemState createState() => _ContactItemState();
}

class _ContactItemState extends State<ContactItem> {
  dynamic parent;
  bool deleted = false; //If it has not been deleted from the backend

  @override
  Widget build(BuildContext context) {
    //final contact = Provider.of<Contact>(context);
    final String rawSvg = Jdenticon.toSvg(
      widget.address,
      colorSaturation: 1.0,
      grayscaleSaturation: 1.0,
      colorLightnessMinValue: 0.40,
      colorLightnessMaxValue: 0.80,
      grayscaleLightnessMinValue: 0.30,
      grayscaleLightnessMaxValue: 0.90,
      backColor: '#ff7800',
      hues: [205],
    );


    return deleted? ListTile(shape: RoundedRectangleBorder(borderRadius: BorderRadius.circular(10),),
      tileColor: Color.fromRGBO(186, 6, 48, 0.5019607843137255),
      title: Text('Removed User'),) :
     ListTile(
      shape: RoundedRectangleBorder(borderRadius: BorderRadius.circular(10),),
      tileColor: Color.fromRGBO(12, 147, 136, 0.5019607843137255),
      title: Text(widget.alias),
      subtitle: Text(widget.address),
      leading:
      SvgPicture.string(
        rawSvg,
        fit: BoxFit.fill,
        height: 32,
        width: 32,
      ),
      trailing: GestureDetector(onTap: () async {

        ContactService cs = ContactService();
        await cs.removeUser(widget.address, widget.list);
        setState(() {
          deleted = true;
          build(context);
        });
        },
        child: Icon(Icons.delete, size: 50,),
      ),
    );

    // return GestureDetector(
    //   onTap: () {
    //     Navigator.of(context).pushNamed(
    //       ViewContractScreen.routeName,
    //       arguments: contract.contractId,
    //     );
    //   },
    //   child: ListTile(
    //     title: Text(contract.title),
    //     leading:
    //     // CircleAvatar(
    //     //   backgroundImage: SvgPicture.
    //     SvgPicture.string(
    //       rawSvg,
    //       fit: BoxFit.fill,
    //       height: 32,
    //       width: 32,
    //     ),
    //   ),
    // );
  }
}
