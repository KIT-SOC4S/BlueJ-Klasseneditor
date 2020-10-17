package class_diagram_editor.diagram;

import class_diagram_editor.code_generation.CodeElement;
import class_diagram_editor.code_generation.JavaCodeGenerator;
import lombok.NonNull;
import lombok.Setter;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Set;

/**
 * Represents an UML representation of a Java interface.
 */
@Setter
public class InterfaceModel implements CodeElement, Connectable {

    private String name;

    private Map<String, Connectable> associations;
    private Set<Connectable> extendsInterfaces;
    private List<MethodModel> methods;

    /**
     * Creates a new {@link InterfaceModel}.
     */
    public InterfaceModel() {
        this.methods = new ArrayList<>();
        this.extendsInterfaces = new HashSet<>();
        this.associations = new HashMap<>();
    }

    @Override
    public void accept(@NonNull JavaCodeGenerator codeGenerator) {
        Objects.requireNonNull(codeGenerator, "codeGenerator cannot be null");

        codeGenerator.visitInterface(this);
    }

    /**
     * @return whether the interface has any methods added.
     */
    public boolean hasMethods() {
        return !methods.isEmpty();
    }

    /**
     * Add a new method to the interface. Adding the same object multiple times is ignored.
     *
     * @param methodModel the {@link MethodModel method} to add.
     */
    public void addMethod(@NonNull MethodModel methodModel) {
        Objects.requireNonNull(methodModel, "methodModel cannot be null");

        methods.add(methodModel);
    }

    /**
     * @return all added {@link MethodModel methods} of the {@link InterfaceModel interface}.
     */
    public List<MethodModel> getMethods() {
        return new ArrayList<>(methods);
    }


    @Override
    public boolean isExtending() {
        return !extendsInterfaces.isEmpty();
    }

    @Override
    public boolean isExtending(Connectable connectable) {
        if (connectable == null) {
            return false;
        }

        for (Connectable extendsType : extendsInterfaces) {
            boolean isExtending = connectable.equals(extendsType);

            if (isExtending) {
                return true;
            }

            boolean transitiveExtending = extendsType.isExtending(connectable);

            if (transitiveExtending) {
                return true;
            }
        }

        return false;
    }

    @Override
    public void addExtendsRelation(@NonNull Connectable extendable) {
        Objects.requireNonNull(extendable, "connectable cannot be null");

        extendsInterfaces.add(extendable);
    }

    @Override
    public void removeExtendsRelation(@NonNull Connectable connectable) {
        Objects.requireNonNull(connectable, "connectable cannot be null");

        extendsInterfaces.remove(connectable);
    }

    @Override
    public Collection<Connectable> getExtendsRelations() {
        return new ArrayList<>(extendsInterfaces);
    }


    @Override
    public boolean hasAssociations() {
        return !associations.isEmpty();
    }

    @Override
    public boolean addAssociation(@NonNull String identifier, @NonNull Connectable connectable) {
        Objects.requireNonNull(identifier, "identifier cannot be null");
        Objects.requireNonNull(connectable, "connectable cannot be null");

        if (getAssociations().containsKey(identifier)) {
            return false;
        }

        associations.put(identifier, connectable);

        return true;
    }

    @Override
    public void removeAssociation(@NonNull String identifier) {
        Objects.requireNonNull(identifier, "identifier cannot be null");

        associations.remove(identifier);
    }

    @Override
    public Map<String, Connectable> getAssociations() {
        Map<String, Connectable> allAssociations = new HashMap<>(associations);

        for (Connectable interfaceModel : extendsInterfaces) {
            allAssociations.putAll(interfaceModel.getAssociations());
        }

        return allAssociations;
    }

    @Override
    public void removeReferencesTo(Connectable removedElement) {
        if (removedElement != null) {
            extendsInterfaces.removeIf(interfaceModel -> interfaceModel.getName().equals(removedElement.getName()));

            associations.entrySet().removeIf(pair -> pair.getValue().getName().equals(removedElement.getName()));
        }
    }


    /**
     * Replaces the associations, extends relations, and methods with
     * the corresponding values of the given {@link InterfaceModel interface}.
     *
     * @param interfaceModel the {@link InterfaceModel interface} to take the new values from.
     */
    public void edit(@NonNull InterfaceModel interfaceModel) {
        Objects.requireNonNull(interfaceModel, "interfaceModel cannot be null");

        this.associations = new HashMap<>(interfaceModel.associations);
        this.extendsInterfaces = new HashSet<>(interfaceModel.extendsInterfaces);
        this.methods = new ArrayList<>(interfaceModel.methods);
    }


    @Override
    public String getName() {
        return name;
    }


    /**
     * @return the name of the {@link InterfaceModel interface}.
     */
    @Override
    public String toString() {
        return name;
    }
}