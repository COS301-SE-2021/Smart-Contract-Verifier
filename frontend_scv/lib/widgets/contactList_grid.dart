//This is a grid of contactlists.
//They should be clickable


import 'package:flutter/material.dart';
import 'package:provider/provider.dart';
import 'package:unison/models/contact.dart';
import 'package:unison/models/contactList.dart';
import 'package:unison/services/Server/contactService.dart';
import 'package:unison/widgets/ContactList_item.dart';

import './contract_item.dart';
import '../models/contracts.dart';
import 'contact_item.dart';

class ContactListGrid extends StatefulWidget {

  ContactListGrid();

  @override
  _ContactListGridState createState() => _ContactListGridState();
}

class _ContactListGridState extends State<ContactListGrid> {

  final _listNameController = TextEditingController();
  final contactServ = ContactService();

  Future<void> _createDial() async {
    await showDialog(context: context, builder: (context) {
      return AlertDialog(title: Text('New Contact List'), actions: [TextButton(
        child: Text('Cancel'),
        onPressed: () {
          // _conditionTitleController.clear();
          _listNameController.clear();
          Navigator.of(context).pop();
        },
      ),
        TextButton(
          child: Text('Save'),
          onPressed: () async {
            // _conditionTitleController.clear();
            await contactServ.createNewList(_listNameController.text);

             _listNameController.clear();
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
              return 'Name:';
            } else {
              return null;
            }
          },
          decoration: InputDecoration(labelText: 'Enter Name'),
          controller: _listNameController,
        ),
      );
    });
  }

  @override
  Widget build(BuildContext context) {
    //-> setting/getting contracts happens in the contracts_overview_screen
    //final contacts = Provider.of<ContactList>(context); //simply access
    // var _firstUser = false;
    // if (contacts == null) {
    //   _firstUser = true;

      ContactService cs = ContactService();
      return Column(
        children: [
          Row(mainAxisAlignment: MainAxisAlignment.center, children: [TextButton(style: TextButton.styleFrom(primary: Color.fromRGBO(
              163, 27, 180, 1.0), textStyle: TextStyle(fontSize: 20)), onPressed: _createDial, child: Text('Create new contact list'))],),
          SizedBox(height: 10,),
          FutureBuilder(future: cs.getContactLists(),builder: (context, AsyncSnapshot snap) {
             if (snap.connectionState != ConnectionState.done) {
                  return CircularProgressIndicator();
              }

             if (snap.data == null)
               return Text('Damn problem');

             List<Widget> ch = [];
             print (snap.data);
              for (var i in snap.data) {
              //  ch.add(Column(children: [Text(i.name), Text(i.id)]));
                ch.add(SizedBox(height: 10));
                ch.add(ContactListItem(i));
              }
            return ListView(children: ch, shrinkWrap: true,);
          }),
        ],
      );

      // ListView.builder(
      //   padding: const EdgeInsets.all(10.0),
      //   itemCount: _firstUser ? 0 : contacts.length,
      //   itemBuilder: (ctx, i) => ChangeNotifierProvider.value(
      //     value: contacts[i],
      //     // child: ContactListItem(),
      //   ),
      // );
    }


    // return ListView.builder(
    //   padding: const EdgeInsets.all(10.0),
    //   itemCount: _firstUser ? 0 : contacts.length,
    //   itemBuilder: (ctx, i) => ChangeNotifierProvider.value(
    //     value: contacts[i],
    //    // child: ContactListItem(),
    //   ),
    // );

}
