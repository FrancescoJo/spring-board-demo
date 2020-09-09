// spring-message-board-demo
// Refer to LICENCE.txt for licence details.
import 'package:flutter/foundation.dart';
import 'package:flutter/widgets.dart';
import 'package:frontend/app/inject/RepositoryModule.dart';
import 'package:frontend/app/ui/MyAppViewModel.dart';
import 'package:frontend/repository/SimpleTextRepository.dart';
import 'package:provider/provider.dart';

/// View Model definitions for application.
///
/// View models are responsible to provide data to the views.
///
/// Author: Francesco Jo(<nimbusob@gmail.com>) <br/>
/// Since: 07 - Sep - 2020
class ViewModelModule {
  final RepositoryModule _repositoryModule;

  ViewModelModule(this._repositoryModule);

  // Since all provider instances are managed by BuildContext scope, we can't rely on singleton approach for
  // all ViewModel instances.
  //
  // All ViewModel creation methods described in 'create' block would be evaluated lazily, and re-executed
  // if necessary by Provider framework.
  @nonVirtual
  Widget bind(final Widget app) {
    return
      MultiProvider(
        providers: [
          // Provider type must be correspondent to supertype of View Model.
          ChangeNotifierProvider(create: (_) => myAppViewModel())
        ],
        child: app,
      );
  }

  IMyAppViewModel myAppViewModel() {
    final ISimpleTextRepository repository = _repositoryModule.simpleTextRepository();

    return IMyAppViewModel.newInstance(repository);
  }
}
