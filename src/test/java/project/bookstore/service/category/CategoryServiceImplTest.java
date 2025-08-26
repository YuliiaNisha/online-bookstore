package project.bookstore.service.category;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertThrows;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoMoreInteractions;
import static org.mockito.Mockito.when;

import java.util.List;
import java.util.Optional;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import project.bookstore.dto.category.CategoryDto;
import project.bookstore.dto.category.CreateCategoryRequestDto;
import project.bookstore.dto.category.UpdateCategoryRequestDto;
import project.bookstore.exception.EntityNotFoundException;
import project.bookstore.mapper.CategoryMapper;
import project.bookstore.model.Category;
import project.bookstore.repository.CategoryRepository;

@ExtendWith(MockitoExtension.class)
class CategoryServiceImplTest {
    public static final long ID = 1L;
    @Mock
    private CategoryMapper categoryMapper;
    @Mock
    private CategoryRepository categoryRepository;
    @InjectMocks
    private CategoryServiceImpl categoryService;
    private final CreateCategoryRequestDto requestDto = createRequestDto();
    private final CategoryDto categoryDto = createCategoryDto();
    private final UpdateCategoryRequestDto updateCategoryRequestDto = createUpdateRequestDto();
    private final PageRequest defaultPageRequest = PageRequest.of(1, 10);
    private Category category;

    @BeforeEach
    void setUp() {
        category = new Category()
                .setId(1L)
                .setName("Fiction")
                .setDescription("Imaginative storytelling including "
                        + "novels, short stories, and fantasy")
                .setDeleted(false);
    }

    @Test
    void createCategory_validRequestDto_returnsCategoryDto() {
        when(categoryMapper.toModel(requestDto)).thenReturn(category);
        when(categoryRepository.save(category)).thenReturn(category);
        when(categoryMapper.toDto(category)).thenReturn(categoryDto);

        CategoryDto actual = categoryService.createCategory(requestDto);

        assertNotNull(actual);
        assertEquals(categoryDto, actual);

        verify(categoryMapper).toModel(requestDto);
        verify(categoryRepository).save(category);
        verify(categoryMapper).toDto(category);
        verifyNoMoreInteractions(categoryMapper, categoryRepository);
    }

    @Test
    void findAll_returnsCategoriesPage() {
        when(categoryRepository.findAll(defaultPageRequest))
                .thenReturn(new PageImpl<>(List.of(category)));
        when(categoryMapper.toDto(category)).thenReturn(categoryDto);

        Page<CategoryDto> actual = categoryService.findAll(defaultPageRequest);

        assertNotNull(actual);
        assertEquals(1, actual.getTotalElements());
        assertEquals(categoryDto, actual.getContent().get(0));

        verify(categoryRepository).findAll(defaultPageRequest);
        verify(categoryMapper).toDto(category);
        verifyNoMoreInteractions(categoryRepository, categoryMapper);
    }

    @Test
    void getCategoryById_validId_returnsCategoryDto() {
        when(categoryRepository.findById(ID)).thenReturn(Optional.of(category));
        when(categoryMapper.toDto(category)).thenReturn(categoryDto);

        CategoryDto actual = categoryService.getCategoryById(ID);

        assertNotNull(actual);
        assertEquals(categoryDto, actual);

        verify(categoryRepository).findById(ID);
        verify(categoryMapper).toDto(category);
        verifyNoMoreInteractions(categoryRepository, categoryMapper);
    }

    @Test
    void getCategoryById_noCategoryById_throwsException() {
        when(categoryRepository.findById(ID)).thenReturn(Optional.empty());

        EntityNotFoundException actual = assertThrows(EntityNotFoundException.class,
                () -> categoryService.getCategoryById(ID));

        assertNotNull(actual);
        assertEquals("There is no category by id: " + ID, actual.getMessage());

        verify(categoryRepository).findById(ID);
        verifyNoMoreInteractions(categoryRepository, categoryMapper);
    }

    @Test
    void update_validId_returnsCategoryDto() {
        when(categoryRepository.findById(ID))
                .thenReturn(Optional.of(category));
        when(categoryRepository.save(category)).thenReturn(category);
        when(categoryMapper.toDto(category)).thenReturn(categoryDto);

        CategoryDto actual = categoryService.update(ID, updateCategoryRequestDto);

        assertNotNull(actual);
        assertEquals(categoryDto, actual);

        verify(categoryRepository).findById(ID);
        verify(categoryMapper).updateCategory(category, updateCategoryRequestDto);
        verify(categoryRepository).save(category);
        verify(categoryMapper).toDto(category);
        verifyNoMoreInteractions(categoryRepository, categoryMapper);
    }

    @Test
    void update_noCategoryById_throwsException() {
        when(categoryRepository.findById(ID))
                .thenReturn(Optional.empty());

        EntityNotFoundException actual = assertThrows(EntityNotFoundException.class,
                () -> categoryService.update(ID, updateCategoryRequestDto));

        assertNotNull(actual);
        assertEquals("There is no category by id: " + ID, actual.getMessage());

        verify(categoryRepository).findById(ID);
        verifyNoMoreInteractions(categoryRepository, categoryMapper);
    }

    @Test
    void delete_validId_returnsCategoryDto() {
        when(categoryRepository.findById(ID)).thenReturn(Optional.of(category));

        verify(categoryRepository).findById(ID);
        verify(categoryRepository).delete(category);
        verifyNoMoreInteractions(categoryRepository);
    }

    @Test
    void delete_noCategoryById_throwsException() {
        when(categoryRepository.findById(ID)).thenReturn(Optional.empty());

        EntityNotFoundException actual = assertThrows(EntityNotFoundException.class,
                () -> categoryService.delete(ID));

        assertNotNull(actual);
        assertEquals("There is no category by id: " + ID,
                actual.getMessage());

        verify(categoryRepository).findById(ID);
        verifyNoMoreInteractions(categoryRepository);
    }

    private CreateCategoryRequestDto createRequestDto() {
        return new CreateCategoryRequestDto(
                "Fiction",
                "Imaginative storytelling including novels, short stories, and fantasy");
    }

    private CategoryDto createCategoryDto() {
        return new CategoryDto(ID, "Fiction", "Imaginative storytelling "
                + "including novels, short stories, and fantasy");
    }

    private UpdateCategoryRequestDto createUpdateRequestDto() {
        return new UpdateCategoryRequestDto("Fiction updated",
                "updated Imaginative storytelling "
                + "including novels, short stories, and fantasy");
    }
}
