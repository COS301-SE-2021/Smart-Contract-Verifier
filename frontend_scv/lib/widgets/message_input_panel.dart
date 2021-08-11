import 'package:flutter/material.dart';
import 'package:unison/models/message.dart';
import 'package:unison/services/Server/messageService.dart';

class MessageInputPanel extends StatefulWidget {
  final String agreementId;
  MessageInputPanel(this.agreementId);

  @override
  _MessageInputPanelState createState() => _MessageInputPanelState();
}

class _MessageInputPanelState extends State<MessageInputPanel> {
  final _messageTextController = TextEditingController();
  var _isLoading = false;
  MessageService messageService = MessageService();
  GlobalKey<FormState> _formKey = GlobalKey<FormState>();
  @override
  void initState() {
    // print('InitState()');
    super.initState();
  }

  Future<void> _saveForm(String agreementId) async {
    final isValid = _formKey.currentState.validate();

    Message newMessage = Message(
      _messageTextController.text,
      agreementId,
    );

    if (!isValid) return;
    _formKey.currentState.save();
    //^^^^saves the form -> executes the 'onSaved' of each input
    setState(() {
      _isLoading = true;
    });

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
      _isLoading = false;
      // Provider.of<Contracts>(context, listen: false).fetchAndSetContracts();
      //TODO: assuming this rebuilds
      super.setState(() {});
    });
  }

  @override
  Widget build(BuildContext context) {
    MessageService messageService = MessageService();
    // print(messages);
    // return Text(messages.toString());
    return Container(
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
                          if (value.isEmpty) return 'Please enter a message.';
                          return null;
                        },
                        onFieldSubmitted: (value) {
                          _saveForm(widget.agreementId);
                        },
                      ),
                    ),
                  ),
                  IconButton(
                    onPressed: () => _saveForm(widget.agreementId),
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
    );
  }
}
