//This is the UI counterpart of Contact from models.

import 'package:flutter/material.dart';
import 'package:provider/provider.dart';
import 'package:jdenticon_dart/jdenticon_dart.dart';
import 'package:flutter_svg/flutter_svg.dart';
import 'package:unison/models/contact.dart';
import 'package:unison/services/Server/contactService.dart';
import 'package:unison/widgets/miscellaneous/jdenticon_svg.dart';

import '../../models/contract.dart';
import '../../providers/auth.dart';
import '../../screens/view_contract_screen.dart';

//Be aware of confusion with Contract (with an r in the middle)
class ContactItem extends StatefulWidget {
  final Contact _contact;
  final String listId; //Which list it is saved in
  final Function _reset;
  ContactItem(this._contact, this.listId, this._reset);

  @override
  _ContactItemState createState() => _ContactItemState();
}

class _ContactItemState extends State<ContactItem> {
  dynamic parent;

  @override
  Widget build(BuildContext context) {
    final String rawSvg = Jdenticon.toSvg(
      widget._contact.address,
      colorSaturation: 1.0,
      grayscaleSaturation: 1.0,
      colorLightnessMinValue: 0.40,
      colorLightnessMaxValue: 0.80,
      grayscaleLightnessMinValue: 0.30,
      grayscaleLightnessMaxValue: 0.90,
      backColor: '#ff7800',
      hues: [205],
    );

    return ListTile(
      title: Text(widget._contact.alias),
      subtitle: Text(widget._contact.address),
      leading: JdenticonSVG(widget._contact.address, [205]),
      trailing: IconButton(
        icon: Icon(Icons.delete),
        onPressed: () async {
          ContactService cs = ContactService();
          await cs.removeUser(widget._contact.address, widget.listId);
          setState(() => widget._reset());
        },
      ),
    );
  }
}
