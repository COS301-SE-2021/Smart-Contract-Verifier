//This is a grid of contactlists.
//They should be clickable

import 'package:awesome_loader/awesome_loader.dart';
import 'package:flutter/material.dart';
import 'package:unison/services/Server/contactService.dart';
import 'package:unison/widgets/contact/ContactList_item.dart';

class ContactListGrid extends StatefulWidget {
  ContactListGrid();

  @override
  _ContactListGridState createState() => _ContactListGridState();
}

class _ContactListGridState extends State<ContactListGrid> {
  final _listNameController = TextEditingController();
  final contactServ = ContactService();

  Future<void> _createDial() async {
    await showDialog(
        context: context,
        builder: (context) {
          return AlertDialog(
            title: Text('New Contact List'),
            actions: [
              TextButton(
                child: Text('Cancel'),
                onPressed: () {
                  _listNameController.clear();
                  Navigator.of(context).pop();
                },
              ),
              TextButton(
                child: Text('Save'),
                onPressed: () async {
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
              decoration: InputDecoration(labelText: 'List Name'),
              controller: _listNameController,
            ),
          );
        });
  }

  @override
  Widget build(BuildContext context) {
    ContactService cs = ContactService();
    return Column(
      // mainAxisSize: MainAxisSize.min,
      children: [
        FutureBuilder(
          future: cs.getContactLists(),
          builder: (context, AsyncSnapshot snap) {
            if (snap.connectionState != ConnectionState.done) {
              return AwesomeLoader(
                loaderType: AwesomeLoader.AwesomeLoader4,
              );
            }
            if (snap.data == null)
              return Text('Could not retrieve '
                  'contact lists, please refresh.');

            List<Widget> ch = [];
            print(snap.data);
            for (var i in snap.data) {
              // ch.add(SizedBox(height: 10));
              ch.add(
                ContactListItem(i),
              );
            }
            return ListView(
              children: ch,
              shrinkWrap: true,
            );
          },
        ),
        Expanded(
          child: Align(
            alignment: Alignment.bottomCenter,
            child: FloatingActionButton.extended(
              onPressed: _createDial,
              label: Text('New Contact List'),
              icon: Icon(Icons.add),
              backgroundColor: Color.fromRGBO(182, 80, 158, 0.8),
            ),
          ),
        ),
        SizedBox(
          height: MediaQuery.of(context).size.height * 0.1,
        )
      ],
    );
  }
}
