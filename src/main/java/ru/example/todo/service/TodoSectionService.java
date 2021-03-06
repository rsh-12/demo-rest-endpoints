package ru.example.todo.service;

import ru.example.todo.domain.TodoSectionProjection;
import ru.example.todo.entity.TodoSection;
import ru.example.todo.entity.TodoTask;
import ru.example.todo.entity.User;
import ru.example.todo.enums.filters.FilterByOperation;

import java.util.List;

/**
 * This interface contains methods for working with TodoSection.
 *
 * @see TodoSection
 */
public interface TodoSectionService {

    /**
     * Finds a section by id.
     *
     * @param userId    the user id
     * @param sectionId the TodoSection id
     * @return the TodoSection
     * @throws ru.example.todo.exception.CustomException if the TodoSection is not found
     * @see TodoSection
     */
    TodoSection findSectionById(Long userId, Long sectionId);

    /**
     * Finds specific fields of the TodoSection by user id:
     * id, title, createdAt, updatedAt.
     *
     * @param userId the user id
     * @return the TodoSectionProjection list
     * @see TodoSectionProjection
     * @see TodoSection
     */
    List<TodoSectionProjection> findSections(Long userId);

    /**
     * Deletes the TodoSection by id. Checks if the Principal
     * equals to User of the TodoSection or Principal has an Admin role,
     * then deletes the TodoSection or throws an exception.
     *
     * @param principal the User
     * @param sectionId the TodoSection id
     * @throws ru.example.todo.exception.CustomException if the TodoSection by id is not found
     *                                                   or if the Principal is not equal to the User
     *                                                   of the TodoSeciton and the Principal does not have
     *                                                   an Admin role
     * @see User
     */
    void deleteSectionById(User principal, Long sectionId);

    /**
     * Creates a new TodoSection.
     *
     * @param user    the User
     * @param section the TodoSection
     * @return the created TodoSection
     * @see TodoSection
     * @see User
     */
    TodoSection createSection(User user, TodoSection section);

    /**
     * Updates the TodoSection by id.
     *
     * @param principal the User
     * @param sectionId the TodoSection id
     * @param section   the TodoSection
     * @return the updated TodoSeciton
     * @throws ru.example.todo.exception.CustomException if the TodoSection by id is not found
     *                                                   or if the Principal is not equal to the User
     *                                                   of the TodoSeciton and the Principal does not have
     *                                                   an Admin role
     * @see TodoSection
     */
    TodoSection updateSection(User principal, Long sectionId, TodoSection section);

    /**
     * Add tasks or removes them from the TodoSection object.
     *
     * @param userId    the User id
     * @param sectionId the TodoSection id
     * @param tasks     the list of tasks
     * @param flag      the flag that contains <b>move</b> or <b>remove</b>
     * @throws ru.example.todo.exception.CustomException if the TodoSection by id and userId is not found
     * @see TodoTask
     * @see TodoSection
     */
    void addTasksToOrRemoveFromSection(Long userId, Long sectionId, List<TodoTask> tasks, FilterByOperation flag);
}
