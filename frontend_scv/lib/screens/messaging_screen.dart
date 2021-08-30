import 'package:flutter/material.dart';
import 'package:unison/models/message.dart';
import 'package:unison/services/Server/messageService.dart';
import 'package:unison/widgets/funky_text_widget.dart';
import 'package:unison/widgets/messages_panel.dart';

class MessagingScreen extends StatefulWidget {
  @override
  _MessagingScreenState createState() => _MessagingScreenState();
  static const routeName = '/messaging-screen';
}

class _MessagingScreenState extends State<MessagingScreen> {
  GlobalKey<FormState> _formKey = GlobalKey<FormState>();
  final _messageTextController = TextEditingController();
  void initState() {
    super.initState();
  }

  MessageService messageService = MessageService();

  Future<void> _saveForm(String agreementId) async {
    final isValid = _formKey.currentState.validate();

    Message newMessage = Message(
      _messageTextController.text,
      agreementId,
    );

    if (!isValid) return;
    _formKey.currentState.save();
    //^^^^saves the form -> executes the 'onSaved' of each input
    setState(() {});

    try {
      //Save to DB:
      await messageService.sendMessage(newMessage);
      print('new message sent');
    } catch (error) {
      await showDialog(
        context: context,
        builder: (ctx) => AlertDialog(
          title: Text('An error occurred!'),
          content: Text('Something went wrong.'),
          actions: <Widget>[
            TextButton(
              child: Text('Okay'),
              onPressed: () {
                Navigator.of(ctx).pop();
              },
            )
          ],
        ),
      );
    }
    setState(() {
      super.setState(() {});
    });
  }

  @override
  Widget build(BuildContext context) {
    final agreementId = ModalRoute.of(context).settings.arguments as String;
    return Scaffold(
      appBar: AppBar(
        title: FunkyText('Agreement Chat: ' + agreementId),
      ),
      // drawer: AppDrawer(),
      body: Column(
        children: [
          Expanded(
            flex: 6,
            // child: MessagesPanel(agreementId, _isInit),
            child: MessagesPanel(
              agreementId,
            ),
          ),
          Expanded(
            flex: 1,
            child: Container(
              padding: EdgeInsets.symmetric(horizontal: 20),
              // color: Colors.white,
              height: 100,
              child: Row(
                children: [
                  Expanded(
                    child: Container(
                      padding: EdgeInsets.symmetric(horizontal: 14),
                      height: 60,
                      decoration: BoxDecoration(
                        border: Border.all(
                          color: Colors.deepOrange,
                        ),
                        borderRadius: BorderRadius.circular(10.0),
                      ),
                      child: Row(
                        children: [
                          SizedBox(
                            width: 10,
                          ),
                          Expanded(
                            child: Form(
                              key: _formKey,
                              child: TextFormField(
                                maxLines: 1,
                                keyboardType: TextInputType.text,
                                controller: _messageTextController,
                                validator: (value) {
                                  if (value.isEmpty)
                                    return 'Please enter a message.';
                                  return null;
                                },
                                onFieldSubmitted: (value) {
                                  _saveForm(agreementId);
                                },
                              ),
                            ),
                          ),
                          IconButton(
                            onPressed: () => _saveForm(agreementId),
                            icon: Icon(Icons.send_outlined),
                          )
                        ],
                      ),
                    ),
                  ),
                  SizedBox(
                    width: 10,
                  ),
                ],
              ),
            ),
          ),
        ],
      ),
    );
  }
}
