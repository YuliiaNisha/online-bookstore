package project.bookstore.service.category;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoMoreInteractions;
import static org.mockito.Mockito.when;

import java.util.List;
import java.util.Optional;
import org.junit.jupiter.api.BeforeAll;
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
import project.bookstore.util.ServiceTestUtil;

@ExtendWith(MockitoExtension.class)
class CategoryServiceImplTest {
    public static final Long ID = 1L;
    private static CreateCategoryRequestDto requestDto;
    private static CategoryDto categoryDto;
    private static PageRequest defaultPageRequest;
    private static UpdateCategoryRequestDto updateCategoryRequestDto;
    @Mock
    private CategoryMapper categoryMapper;
    @Mock
    private CategoryRepository categoryRepository;
    @InjectMocks
    private CategoryServiceImpl categoryService;
    private Category categoryWithId;
    private Category categoryWithoutId;

    @BeforeAll
    static void beforeAll() {
        requestDto = ServiceTestUtil.getCreateFictionCategoryRequestDto();
        categoryDto = ServiceTestUtil.getFictionCategoryDto();
        updateCategoryRequestDto = ServiceTestUtil.getUpdateFictionCategoryRequestDto();
        defaultPageRequest = ServiceTestUtil.getDefaultPageRequest();
    }

    @BeforeEach
    void setUp() {
        categoryWithId = ServiceTestUtil.getFictionCategory();
        categoryWithoutId = ServiceTestUtil.getFictionCategoryWithoutId();
    }

    @Test
    void createCategory_validRequestDto_returnsCategoryDto() {
        when(categoryMapper.toModel(requestDto)).thenReturn(categoryWithoutId);
        when(categoryRepository.save(categoryWithoutId)).thenReturn(categoryWithId);
        when(categoryMapper.toDto(categoryWithId)).thenReturn(categoryDto);

        CategoryDto actual = categoryService.createCategory(requestDto);

        assertNotNull(actual);
        assertEquals(categoryDto, actual);

        verify(categoryMapper).toModel(requestDto);
        verify(categoryRepository).save(categoryWithoutId);
        verify(categoryMapper).toDto(categoryWithId);
        verifyNoMoreInteractions(categoryMapper, categoryRepository);
    }

    @Test
    void findAll_returnsCategoriesPage() {
        when(categoryRepository.findAll(defaultPageRequest))
                .thenReturn(new PageImpl<>(List.of(categoryWithId)));
        when(categoryMapper.toDto(categoryWithId)).thenReturn(categoryDto);

        Page<CategoryDto> actual = categoryService.findAll(defaultPageRequest);

        assertNotNull(actual);
        assertEquals(1, actual.getTotalElements());
        assertEquals(categoryDto, actual.getContent().get(0));

        verify(categoryRepository).findAll(defaultPageRequest);
        verify(categoryMapper).toDto(categoryWithId);
        verifyNoMoreInteractions(categoryRepository, categoryMapper);
    }

    @Test
    void getCategoryById_validId_returnsCategoryDto() {
        when(categoryRepository.findById(ID)).thenReturn(Optional.of(categoryWithId));
        when(categoryMapper.toDto(categoryWithId)).thenReturn(categoryDto);

        CategoryDto actual = categoryService.getCategoryById(ID);

        assertNotNull(actual);
        assertEquals(categoryDto, actual);

        verify(categoryRepository).findById(ID);
        verify(categoryMapper).toDto(categoryWithId);
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
                .thenReturn(Optional.of(categoryWithId));
        when(categoryRepository.save(categoryWithId)).thenReturn(categoryWithId);
        when(categoryMapper.toDto(categoryWithId)).thenReturn(categoryDto);

        CategoryDto actual = categoryService.update(ID, updateCategoryRequestDto);

        assertNotNull(actual);
        assertEquals(categoryDto, actual);

        verify(categoryRepository).findById(ID);
        verify(categoryMapper).updateCategory(categoryWithId, updateCategoryRequestDto);
        verify(categoryRepository).save(categoryWithId);
        verify(categoryMapper).toDto(categoryWithId);
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
    void delete_validId_deletesCategory() {
        when(categoryRepository.findById(ID)).thenReturn(Optional.of(categoryWithId));

        categoryService.delete(ID);

        verify(categoryRepository).findById(ID);
        verify(categoryRepository).delete(categoryWithId);
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
}
