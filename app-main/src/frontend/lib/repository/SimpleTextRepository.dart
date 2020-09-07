// Template file to demonstrate DI in Flutter
import 'package:frontend/dataSrc/SimpleTextDataSource.dart';

abstract class ISimpleTextRepository {
  String getText();

  static ISimpleTextRepository newInstance(final ISimpleTextDataSource dataSrc) =>
      SimpleTextRepository(dataSrc);
}

class SimpleTextRepository extends ISimpleTextRepository {
  final ISimpleTextDataSource _dataSrc;

  SimpleTextRepository(this._dataSrc);

  @override
  String getText() {
    return _dataSrc.getText();
  }
}
