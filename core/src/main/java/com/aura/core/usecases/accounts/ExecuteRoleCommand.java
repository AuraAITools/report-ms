package com.aura.core.usecases.accounts;

import java.util.List;

public class ExecuteRoleCommand {
    List<AssignRoleUseCase> assignRoleCommands;

    public ExecuteRoleCommand add(AssignRoleUseCase assignRoleUseCase) {
        assignRoleCommands.add(assignRoleUseCase);
        return this;
    }

    public void execute() {
        assignRoleCommands.forEach(AssignRoleUseCase::assignRole);
    }
}
