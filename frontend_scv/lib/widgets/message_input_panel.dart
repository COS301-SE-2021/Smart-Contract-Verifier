import 'package:awesome_loader/awesome_loader.dart';
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
      _messageTextController.clear();
      _messageTextController.clearComposing();
      _isLoading = false;
    });

    // setState(() {
    //   super.setState(() {});
    // });
  }

  @override
  Widget build(BuildContext context) {
    return Container(
      margin: EdgeInsets.all(16.0),
      // padding: EdgeInsets.symmetric(horizontal: 20),
      height: 100,
      child: Row(
        children: [
          Expanded(
            child: Container(
              // padding: EdgeInsets.symmetric(horizontal: 14),
              // height: 60,

              decoration: BoxDecoration(
                  // color: Colors.grey,
                  color: Color.fromRGBO(43, 45, 60, 1),
                  shape: BoxShape.rectangle,
                  borderRadius: BorderRadius.circular(50),
                  boxShadow: [
                    BoxShadow(
                      color: Colors.pink,
                      blurRadius: 5.0,
                      spreadRadius: 2.0,
                    ),
                  ]),
              child: Row(
                children: [
                  SizedBox(
                    width: 10,
                  ),
                  Expanded(
                    child: Form(
                      key: _formKey,
                      child: TextFormField(
                        decoration: InputDecoration(
                            border: InputBorder.none,
                            focusedBorder: InputBorder.none,
                            enabledBorder: InputBorder.none,
                            errorBorder: InputBorder.none,
                            disabledBorder: InputBorder.none,
                            contentPadding: EdgeInsets.only(
                                left: 15, bottom: 11, top: 11, right: 15),
                            hintText: "Enter a message..."),
                        maxLines: 1,
                        keyboardType: TextInputType.text,
                        controller: _messageTextController,
                        validator: (value) {
                          if (value.isEmpty)
                            return 'Please send something '
                                'meaningful';
                          return null;
                        },
                        onFieldSubmitted: (value) {
                          _saveForm(widget.agreementId);
                        },
                      ),
                    ),
                  ),
                  if (!_isLoading)
                    IconButton(
                      onPressed: _isLoading
                          ? null
                          : () => _saveForm(widget.agreementId),
                      icon: Icon(
                        Icons.send_outlined,
                        color: Colors.pinkAccent,
                      ),
                    ),
                ],
              ),
            ),
          )
        ],
      ),
    );
  }
}
