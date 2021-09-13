import 'package:flutter/material.dart';

class GlowOnHover extends StatefulWidget {
  final Widget child;
  GlowOnHover(this.child);
  @override
  _GlowOnHoverState createState() => _GlowOnHoverState();
}

class _GlowOnHoverState extends State<GlowOnHover> {
  bool _hovering = false;
  @override
  Widget build(BuildContext context) {
    return MouseRegion(
      onEnter: (e) => _mouseEnter(true),
      onExit: (e) => _mouseEnter(false),
      child: AnimatedContainer(
        duration: Duration(milliseconds: 50),
        decoration: BoxDecoration(
            // color: Colors.grey,
            // shape: BoxShape.circle,
            boxShadow: !_hovering
                ? null
                : [
                    BoxShadow(
                      color: Color.fromRGBO(182, 80, 158, 0.8),
                      blurRadius: 5.0,
                      spreadRadius: 0.1,
                    ),
                  ]),
        child: widget.child,
      ),
    );
  }

  void _mouseEnter(bool hovering) {
    setState(() {
      _hovering = hovering;
    });
  }
}
