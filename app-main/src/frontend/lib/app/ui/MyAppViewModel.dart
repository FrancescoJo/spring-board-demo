// Template file to demonstrate DI in Flutter
import 'package:flutter/foundation.dart';
import 'package:frontend/repository/SimpleTextRepository.dart';

abstract class IMyAppViewModel extends ChangeNotifier {
  String getText();

  static IMyAppViewModel newInstance(final ISimpleTextRepository repo) =>
      _MyAppViewModel(repo);
}

class _MyAppViewModel extends IMyAppViewModel {
  final ISimpleTextRepository _repo;

  _MyAppViewModel(this._repo);

  @override
  String getText() {
    return _repo.getText();
  }
}
