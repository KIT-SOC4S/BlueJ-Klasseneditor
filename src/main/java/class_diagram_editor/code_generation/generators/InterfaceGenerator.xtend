package class_diagram_editor.code_generation.generators

import class_diagram_editor.diagram.InterfaceModel;
import class_diagram_editor.diagram.MethodModel;
import class_diagram_editor.diagram.Extendable;

class InterfaceGenerator extends Generator<InterfaceModel> {

    override String generate(InterfaceModel interfaceModel) '''
        public interface «interfaceModel.getName()»«IF interfaceModel.isExtending()» extends «FOR Extendable extendable : interfaceModel.getExtendsRelations() SEPARATOR ', '»«extendable.getName()»«ENDFOR»«ENDIF» {
            «FOR MethodModel methodModel : interfaceModel.getMethods() SEPARATOR '\n'»
                «generateMethodSignature(methodModel).trim()»;
            «ENDFOR»
        }
    '''

}