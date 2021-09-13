//Not to be confused with view contract screen
//
import 'package:flutter/material.dart';
import 'package:unison/services/Server/contactService.dart';
import 'package:unison/widgets/contact/contact_item.dart';

class ViewContactScreen extends StatefulWidget {
  static const routeName = '/view-contact';

  @override
  _ViewContactScreenState createState() => _ViewContactScreenState();
}

class _ViewContactScreenState extends State<ViewContactScreen> {
  final TextEditingController _conAddrController = TextEditingController();
  final TextEditingController _conNameController = TextEditingController();
  final ContactService cs = ContactService();
  String id;
  String name;

  @override
  void initState() {
    super.initState();
  }

  Future<void> _createDial() async {
    await showDialog(
        context: context,
        builder: (context) {
          _conNameController.clear();
          _conAddrController.clear();
          return AlertDialog(
            content: Container(
              child: Column(mainAxisSize: MainAxisSize.min, children: [
                TextFormField(
                  validator: (value) {
                    if (value.isEmpty) {
                      return 'Name:';
                    } else {
                      return null;
                    }
                  },
                  decoration: InputDecoration(labelText: 'Enter Contact Name:'),
                  controller: _conNameController,
                ),
                TextFormField(
                  validator: (value) {
                    if (value.isEmpty) {
                      return 'Address:';
                    } else {
                      return null;
                    }
                  },
                  decoration:
                      InputDecoration(labelText: 'Enter Wallet Address:'),
                  controller: _conAddrController,
                ),
              ]),
            ),
            title: Text('Add Contact'),
            actions: [
              TextButton(
                child: Text('Cancel'),
                onPressed: () {
                  Navigator.of(context).pop();
                },
              ),
              TextButton(
                child: Text('Save'),
                onPressed: () {
                  Navigator.of(context).pop();
                },
              ),
            ],
          );
        });

    await cs.addUser(
      _conAddrController.text.toLowerCase(),
      _conNameController.text,
      id,
    ); //Get from
    // above
  }

  Widget build(BuildContext context) {
    final args = ModalRoute.of(context).settings.arguments as List<dynamic>;
    id = args[0];
    name = args[1];

    return Scaffold(
        appBar: AppBar(
          title: Text(name),
        ),
        body: Column(
          children: [
            TextButton(
              style: TextButton.styleFrom(
                  primary: Color.fromRGBO(182, 20, 180, 1.0)),
              child: Text(
                'Add a contact to "$name"',
                style: TextStyle(fontSize: 20),
              ),
              onPressed: () async {
                await _createDial();
                setState(() {
                  //TODO: Surely this can be more efficient.... Good luck Kevin
                  build(context);
                });
              },
            ),
            FutureBuilder(
                future: cs.getContacts(id),
                builder: (context, AsyncSnapshot snap) {
                  if (snap.connectionState != ConnectionState.done) {
                    return CircularProgressIndicator();
                  }
                  if (snap.data == null) return Text('Big problem');

                  List<Widget> ch = [];
                  print(snap.data);
                  for (var i in snap.data) {
                    //  ch.add(Column(children: [Text(i.name), Text(i.id)]));
                    ch.add(SizedBox(height: 10));
                    ch.add(ContactItem(i, id));
                  }
                  return ListView(
                    children: ch,
                    shrinkWrap: true,
                  );
                }),
          ],
        ));
  }
}
