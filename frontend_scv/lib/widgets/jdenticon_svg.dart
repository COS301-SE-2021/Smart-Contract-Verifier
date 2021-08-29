import 'package:flutter/material.dart';
import 'package:flutter_svg/svg.dart';
import 'package:jdenticon_dart/jdenticon_dart.dart';

class JdenticonSVG extends StatelessWidget {
  final String _inputString;
  final String colorHash;

  JdenticonSVG(this._inputString, this.colorHash);

  @override
  Widget build(BuildContext context) {
    String rawSvg = Jdenticon.toSvg(
      _inputString,
      colorSaturation: 1.0,
      grayscaleSaturation: 1.0,
      colorLightnessMinValue: 0.40,
      colorLightnessMaxValue: 0.80,
      grayscaleLightnessMinValue: 0.30,
      grayscaleLightnessMaxValue: 0.90,
      backColor: colorHash,
      hues: [205],
    ).toString();
    return SvgPicture.string(
      rawSvg,
      fit: BoxFit.fill,
      height: 32,
      width: 32,
    );
  }
}
