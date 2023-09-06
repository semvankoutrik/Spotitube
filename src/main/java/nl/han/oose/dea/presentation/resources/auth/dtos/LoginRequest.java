package nl.han.oose.dea.presentation.resources.auth.dtos;

import jakarta.validation.constraints.NotNull;

public record LoginRequest(@NotNull String user, @NotNull String password) { }
