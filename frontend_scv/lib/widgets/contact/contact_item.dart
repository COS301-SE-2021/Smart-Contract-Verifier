//This is the UI counterpart of Contact from models.

import 'package:flutter/material.dart';
import 'package:flutter/services.dart';
import 'package:unison/models/contact.dart';
import 'package:unison/services/Server/contactService.dart';
import 'package:unison/widgets/miscellaneous/jdenticon_svg.dart';

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
  final snackBar = SnackBar(
    backgroundColor: Color.fromRGBO(56, 61, 81, 1),
    content: Row(mainAxisAlignment: MainAxisAlignment.center, children: [
      Text(
        'Address Copied to Clipboard',
        style: TextStyle(color: Colors.pink, fontSize: 16),
      )
    ]),
  );

  @override
  Widget build(BuildContext context) {
    return ListTile(
      title: Text(widget._contact.alias),
      subtitle: Text(widget._contact.address),
      leading: JdenticonSVG(widget._contact.address, [205]),
      trailing: Row(
        // mainAxisAlignment: MainAxisAlignment.spaceAround,
        mainAxisSize: MainAxisSize.min,
        children: [
          IconButton(
            color: Colors.blue,
            onPressed: () async {
              ClipboardData data =
                  ClipboardData(text: '<Text to copy goes here>');
              await Clipboard.setData(data);

              Clipboard.setData(
                ClipboardData(text: widget._contact.address),
              );
              ScaffoldMessenger.of(context).showSnackBar(snackBar);
            },
            icon: Icon(
              Icons.copy,
              color: Colors.grey,
            ),
          ),
          SizedBox(
            width: MediaQuery.of(context).size.width * 0.05,
          ),
          IconButton(
            icon: Icon(
              Icons.delete,
              color: Colors.grey,
            ),
            onPressed: () async {
              ContactService cs = ContactService();
              await cs.removeUser(widget._contact.address, widget.listId);
              setState(() => widget._reset());
            },
          ),
        ],
      ),
    );
  }
}
