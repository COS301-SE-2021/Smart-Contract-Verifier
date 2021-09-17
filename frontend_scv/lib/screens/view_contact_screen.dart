//Not to be confused with view contract screen
//
import 'package:animated_text_kit/animated_text_kit.dart';
import 'package:awesome_loader/awesome_loader.dart';
import 'package:flutter/material.dart';
import 'package:unison/services/Server/contactService.dart';
import 'package:unison/widgets/contact/contact_item.dart';
import 'package:unison/widgets/miscellaneous/funky_text_widget.dart';

class ViewContactScreen extends StatefulWidget {
  static const routeName = '/view-contact';

  @override
  _ViewContactScreenState createState() => _ViewContactScreenState();
}

class _ViewContactScreenState extends State<ViewContactScreen> {
  final TextEditingController _conAddressController = TextEditingController();
  final TextEditingController _conNameController = TextEditingController();
  final ContactService cs = ContactService();
  String id;
  String name;

  @override
  void initState() {
    super.initState();
  }

  void reset() {
    setState(() {});
  }

  Future<void> _createDial() async {
    await showDialog(
        context: context,
        builder: (context) {
          _conNameController.clear();
          _conAddressController.clear();
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
                      return 'Please enter an address';
                    } else {
                      return null;
                    }
                  },
                  decoration:
                      InputDecoration(labelText: 'Enter Wallet Address:'),
                  controller: _conAddressController,
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
      _conAddressController.text.toLowerCase(),
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
        title: FunkyText(name + ' List'),
      ),
      body: Column(
        children: [
          FutureBuilder(
            future: cs.getContacts(id),
            builder: (context, AsyncSnapshot snap) {
              if (snap.connectionState != ConnectionState.done) {
                return AwesomeLoader(
                  loaderType: AwesomeLoader.AwesomeLoader4,
                );
              }
              if (snap.data == null)
                return Text('Could not retrieve '
                    'contacts for this list, please refresh.');

              List<Widget> ch = [];
              for (var i in snap.data) {
                ch.add(ContactItem(i, id, reset));
              }
              if (snap.data.length == 0) {
                return Padding(
                  padding: const EdgeInsets.all(8.0),
                  child: SizedBox(
                    child: DefaultTextStyle(
                      style: const TextStyle(
                        fontSize: 14,
                        color: Colors.pinkAccent,
                        shadows: [
                          Shadow(
                            blurRadius: 7.0,
                            color: Colors.pinkAccent,
                            offset: Offset(0, 0),
                          ),
                        ],
                      ),
                      child: AnimatedTextKit(
                        repeatForever: true,
                        animatedTexts: [
                          FlickerAnimatedText(
                            'No Contacts in this list.',
                            textAlign: TextAlign.center,
                          ),
                          FlickerAnimatedText(
                            'You can add contacts with the button below.',
                            textAlign: TextAlign.center,
                          ),
                        ],
                      ),
                    ),
                  ),
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
                onPressed: () async {
                  await _createDial();
                  setState(() {
                    build(context);
                  });
                },
                label: Text('Add a contact to "$name"'),
                icon: Icon(Icons.add),
                backgroundColor: Color.fromRGBO(182, 80, 158, 0.8),
              ),
            ),
          ),
          SizedBox(
            height: MediaQuery.of(context).size.height * 0.1,
          )
        ],
      ),
    );
  }
}
